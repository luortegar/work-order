import { useEffect, useState, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useForm, Controller } from 'react-hook-form';
import {
  Container,
  Button,
  TextField,
  Stack,
  Typography,
  Box,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  CircularProgress,
  useTheme, Grid
} from '@mui/material';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import dayjs from 'dayjs';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';
import { view, update, create, deleteById, uploadPhoto } from 'src/api/workOrderApi';
import { autocompleteBranch } from 'src/api/branchApi';
import { autocompleteEquipmentAndFilterByBranchId } from 'src/api/equipmentApi';
import { autocompleteEmployeesAndFilterByBranchId } from 'src/api/employeesApi';
import { autocompleteTechnicians } from 'src/api/techniciansApi';
import { BranchAutocompleteResponse } from 'src/api/types/branchTypes';
import { EquipmentAutocompleteResponse } from 'src/api/types/equitmentTypes';
import { UserAutocompleteResponse } from 'src/api/types/employeesTypes';
import ControlledAutocomplete from 'src/components/custom-field/ControlledAutocomplete';
import ControlledDropzone from 'src/components/custom-field/ControlledDropzone';
import ControlledSignaturePad from 'src/components/custom-field/ControlledSignaturePad';

interface WorkOrderForm {
  workOrderId: string;
  workOrderNumber: string;
  clientId: string;
  branchId: string | null;
  recipientId: string | null;
  technicianId: string | null;
  equipmentId: string | null;
  serviceDetails: string;
  observations: string;
  photoIdList: string[];
  startTime: dayjs.Dayjs | null;
  endTime: dayjs.Dayjs | null;
}

export default function WorkOrderView() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { showMessage } = useSnackbar();
  const theme = useTheme();

  const isCreating = id === 'new';
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
  const [deleteConfirmationText, setDeleteConfirmationText] = useState('');

  const [branchOptions, setBranchOptions] = useState<BranchAutocompleteResponse[]>([]);
  const [equipmentOptions, setEquipmentOptions] = useState<EquipmentAutocompleteResponse[]>([]);
  const [employeesOptions, setEmployeesOptions] = useState<UserAutocompleteResponse[]>([]);
  const [techniciansOptions, setTechniciansOptions] = useState<UserAutocompleteResponse[]>([]);

  const { register, handleSubmit, reset, control, watch, setValue } = useForm<WorkOrderForm>({
    defaultValues: {
      workOrderId: '',
      workOrderNumber: '',
      clientId: '',
      branchId: null,
      recipientId: null,
      technicianId: null,
      equipmentId: null,
      serviceDetails: '',
      observations: '',
      photoIdList: [],
      startTime: null,
      endTime: null,
    },
  });

  const branchId = watch('branchId');

  const fetchBranch = useCallback((q: string) => {
    autocompleteBranch(q).then(setBranchOptions);
  }, []);

  const fetchEquipment = useCallback(
    (q: string) => branchId && autocompleteEquipmentAndFilterByBranchId(branchId, q).then(setEquipmentOptions),
    [branchId]
  );

  const fetchEmployees = useCallback(
    (q: string) => branchId && autocompleteEmployeesAndFilterByBranchId(branchId, q).then(setEmployeesOptions),
    [branchId]
  );

  const fetchTechnicians = useCallback((q: string) => {
    autocompleteTechnicians(q).then(setTechniciansOptions);
  }, []);

  const uploadFile = (file:File) => {
    if(id){
      uploadPhoto(file,id )
    }
  };

  useEffect(() => {
    const load = async () => {
      if (isCreating) return setLoading(false);
      if (!id) return;

      try {
        const response = await view(id);
        const [branches, techs] = await Promise.all([autocompleteBranch(''), autocompleteTechnicians('')]);
        setBranchOptions(branches);
        setTechniciansOptions(techs);

        if (response.branchId) {
          const [equip, emp] = await Promise.all([
            autocompleteEquipmentAndFilterByBranchId(response.branchId, ''),
            autocompleteEmployeesAndFilterByBranchId(response.branchId, ''),
          ]);
          setEquipmentOptions(equip);
          setEmployeesOptions(emp);
        }

        reset({
          ...response,
          startTime: response.startTime ? dayjs(response.startTime) : null,
          endTime: response.endTime ? dayjs(response.endTime) : null,
        });
      } catch {
        showMessage('Failed to load work order data', 'error');
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [id, isCreating, reset, showMessage]);

  useEffect(() => {
    if (!branchId) {
      setEquipmentOptions([]);
      setEmployeesOptions([]);
      return;
    }
    setValue('equipmentId', null);
    setValue('recipientId', null);
    fetchEquipment('');
    fetchEmployees('');
  }, [branchId, setValue, fetchEquipment, fetchEmployees]);

  const handleSave = async (data: WorkOrderForm) => {
    const payload = {
      ...data,
      startTime: data.startTime?.format('YYYY-MM-DD HH:mm:ss') || null,
      endTime: data.endTime?.format('YYYY-MM-DD HH:mm:ss') || null,
    };
    try {
      if (isCreating) {
        await create(payload);
        showMessage('Created successfully.', 'success');
      } else if (id) {
        await update(id, payload);
        showMessage('Updated successfully.', 'success');
      }
      setEditing(false);
      navigate('/home/work-order');
    } catch {
      showMessage('Failed to save.', 'error');
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '70vh' }}>
        <CircularProgress size={60} />
      </Box>
    );
  }

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Container maxWidth="xl">
        {/* Encabezado */}
        <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
          <Typography variant="h4">{isCreating ? 'Create Work order' : 'Work order'}</Typography>
          <Stack direction="row" spacing={2}>
            {editing || isCreating ? (
              <>
                <Button variant="contained" onClick={handleSubmit(handleSave)}>
                  {isCreating ? 'Create' : 'Save'}
                </Button>
                <Button variant="outlined" onClick={() => (isCreating ? navigate('/home/work-order') : setEditing(false))}>
                  Cancel
                </Button>
              </>
            ) : (
              <>
                <Button variant="contained" color="warning" onClick={() => setOpenConfirmDialog(true)}>
                  Delete
                </Button>
                <Button variant="contained" onClick={() => setEditing(true)}>
                  Edit
                </Button>
                <Button variant="contained" color="inherit" onClick={() => navigate('/home/work-order')}>
                  Back to List
                </Button>
              </>
            )}
          </Stack>
        </Stack>

        {/* Formulario */}
        <Box
          component="form"
          sx={{
            p: 3,
            borderRadius: 3,
            boxShadow: '0px 6px 20px rgba(0,0,0,0.05)',
            backgroundColor: theme.palette.background.paper,
          }}
        >
          <Grid container spacing={3}>
            <Grid size={12}>
              <TextField
                label="Work Order Number"
                {...register('workOrderNumber')}
                fullWidth
                disabled={!editing && !isCreating}
              />
            </Grid>

            <Grid size={{ xs: 12, md: 6 }}>
              <Stack spacing={2}>
                <ControlledAutocomplete
                  name="branchId"
                  control={control}
                  options={branchOptions}
                  optionKey="branchId"
                  labelKey="branchFullName"
                  label="Branch"
                  fetchOptions={fetchBranch}
                  disabled={!editing && !isCreating}
                />
              </Stack>
            </Grid>

            <Grid size={{ xs: 12, md: 6 }}>
              <Stack spacing={2}>
                <ControlledAutocomplete
                  name="technicianId"
                  control={control}
                  options={techniciansOptions}
                  optionKey="employeeId"
                  labelKey="employeeFullName"
                  label="Technician"
                  fetchOptions={fetchTechnicians}
                  disabled={!editing && !isCreating}
                />
              </Stack>
            </Grid>

            {branchId && (
              <Grid size={{ xs: 12, md: 6 }}>
                <Stack spacing={2}>
                  <ControlledAutocomplete
                    name="equipmentId"
                    control={control}
                    options={equipmentOptions}
                    optionKey="equipmentId"
                    labelKey="equipmentFullName"
                    label="Equipment"
                    fetchOptions={fetchEquipment}
                    disabled={!editing && !isCreating}
                  />
                </Stack>
              </Grid>
            )}


            {branchId && (
              <Grid size={{ xs: 12, md: 6 }}>
                <Stack spacing={2}>
                  <ControlledAutocomplete
                    name="recipientId"
                    control={control}
                    options={employeesOptions}
                    optionKey="employeeId"
                    labelKey="employeeFullName"
                    label="Recipient"
                    fetchOptions={fetchEmployees}
                    disabled={!editing && !isCreating}
                  />
                </Stack>
              </Grid>
            )}

            <Grid size={12}>
              <TextField
                label="Service Details"
                {...register('serviceDetails')}
                multiline
                rows={3}
                fullWidth
                disabled={!editing && !isCreating}
              />
            </Grid>

            <Grid size={12}>
              <TextField
                label="Observations"
                {...register('observations')}
                multiline
                rows={2}
                fullWidth
                disabled={!editing && !isCreating}
              />
            </Grid>

            <Grid size={{ xs: 12, md: 6 }}>
              <Stack spacing={2}>
                <Controller
                  name="startTime"
                  control={control}
                  render={({ field }) => (
                    <DateTimePicker label="Start Time" {...field} disabled={!editing && !isCreating} />
                  )}
                />
              </Stack>
            </Grid>

            <Grid size={{ xs: 12, md: 6 }}>
              <Stack spacing={2}>
                <Controller
                  name="endTime"
                  control={control}
                  render={({ field }) => (
                    <DateTimePicker label="End Time" {...field} disabled={!editing && !isCreating} />
                  )}
                />
              </Stack>
            </Grid>

            <Grid size={{ xs: 12, md: 6 }}>
              <Stack
                spacing={2}
                direction={{ xs: 'column', md: 'row' }}
                alignItems="stretch"
              >
                <ControlledSignaturePad label="Recipient signature" name="recipientSignatureBase64" control={control} />
              </Stack>
            </Grid>
            <Grid size={{ xs: 12, md: 6 }}>
              <Stack
                spacing={2}
                direction={{ xs: 'column', md: 'row' }}
                alignItems="stretch"
              >
                <ControlledSignaturePad label="Technician signature" name="technicianSignatureBase64" control={control} />
              </Stack>
            </Grid>

            <Grid size={12}>
              <ControlledDropzone uploadFile={uploadFile} name="profileImage" control={control} label="Drag or select your file." />
            </Grid>
          </Grid>
        </Box>

        {/* Diálogo de confirmación */}
        <Dialog open={openConfirmDialog} onClose={() => setOpenConfirmDialog(false)}>
          <DialogTitle>Confirm Delete</DialogTitle>
          <DialogContent>
            <Typography>Type "delete" to confirm deletion:</Typography>
            <TextField
              value={deleteConfirmationText}
              onChange={(e) => setDeleteConfirmationText(e.target.value)}
              fullWidth
              margin="dense"
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpenConfirmDialog(false)}>Cancel</Button>
            <Button
              color="error"
              onClick={async () => {
                if (id) {
                  await deleteById(id);
                  showMessage('Deleted successfully.', 'success');
                  navigate('/home/work-order');
                }
              }}
              disabled={deleteConfirmationText !== 'delete'}
            >
              Delete
            </Button>
          </DialogActions>
        </Dialog>
      </Container>
    </LocalizationProvider>
  );
}
