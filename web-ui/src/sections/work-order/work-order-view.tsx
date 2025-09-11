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
  useTheme,
} from '@mui/material';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import dayjs from 'dayjs';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';
import { view, update, create, deleteById } from 'src/api/workOrderApi';
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

// ----------------------
// Tipos
// ----------------------
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

  // Los estados de los inputs para cada Autocomplete ya no son necesarios
  // const [branchInput, setBranchInput] = useState('');
  // const [equipmentInput, setEquipmentInput] = useState('');
  // const [recipientInput, setRecipientInput] = useState('');
  // const [technicianInput, setTechnicianInput] = useState('');

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

  // ----------------------
  // Funciones de fetch para Autocomplete
  // Se usa useCallback para optimizar y evitar re-creaciones innecesarias
  // ----------------------
  const fetchBranch = useCallback((query: string) => {
    autocompleteBranch(query).then(setBranchOptions);
  }, []);

  const fetchEquipment = useCallback((query: string) => {
    if (branchId) {
      autocompleteEquipmentAndFilterByBranchId(branchId, query).then(setEquipmentOptions);
    }
  }, [branchId]);

  const fetchEmployees = useCallback((query: string) => {
    if (branchId) {
      autocompleteEmployeesAndFilterByBranchId(branchId, query).then(setEmployeesOptions);
    }
  }, [branchId]);

  const fetchTechnicians = useCallback((query: string) => {
    autocompleteTechnicians(query).then(setTechniciansOptions);
  }, []);

  // ----------------------
  // Carga inicial de datos
  // ----------------------
  useEffect(() => {
    const fetchData = async () => {
      if (isCreating) {
        setLoading(false);
        return;
      }

      if (id) {
        try {
          const response = await view(id);

          // Carga inicial de opciones para que el componente las pueda encontrar al precargar.
          const initialBranchOptions = await autocompleteBranch('');
          setBranchOptions(initialBranchOptions);
          const initialTechnicianOptions = await autocompleteTechnicians('');
          setTechniciansOptions(initialTechnicianOptions);

          if (response.branchId) {
            const initialEquipmentOptions = await autocompleteEquipmentAndFilterByBranchId(response.branchId, '');
            setEquipmentOptions(initialEquipmentOptions);
            const initialEmployeesOptions = await autocompleteEmployeesAndFilterByBranchId(response.branchId, '');
            setEmployeesOptions(initialEmployeesOptions);
          }

          reset({
            ...response,
            startTime: response.startTime ? dayjs(response.startTime) : null,
            endTime: response.endTime ? dayjs(response.endTime) : null,
          });

          setLoading(false);
        } catch (error) {
          showMessage('Failed to load work order data', 'error');
          setLoading(false);
        }
      }
    };

    fetchData();
  }, [id, isCreating, showMessage, reset]);

  // ----------------------
  // Cargar options según branchId
  // ----------------------
  useEffect(() => {
    const fetchRelatedOptions = async () => {
      if (branchId) {
        setValue('equipmentId', null);
        setValue('recipientId', null);
        fetchEquipment('');
        fetchEmployees('');
      } else {
        setEquipmentOptions([]);
        setEmployeesOptions([]);
      }
    };

    fetchRelatedOptions();
  }, [branchId, setValue, fetchEquipment, fetchEmployees]);


  // ----------------------
  // Cargar options según branchId
  // ----------------------
  useEffect(() => {
    const fetchRelatedOptions = async () => {
      if (branchId) {
        setValue('equipmentId', null);
        setValue('recipientId', null);
        fetchEquipment('');
        fetchEmployees('');
      } else {
        setEquipmentOptions([]);
        setEmployeesOptions([]);
      }
    };

    fetchRelatedOptions();
  }, [branchId, setValue, fetchEquipment, fetchEmployees]);

  // ----------------------
  // Handlers de formulario
  // ----------------------
  const handleEdit = () => setEditing(true);

  const handleSave = async (formData: WorkOrderForm) => {
    try {
      const dataToSend = {
        ...formData,
        startTime: formData.startTime?.format('YYYY-MM-DD HH:mm:ss') || null,
        endTime: formData.endTime?.format('YYYY-MM-DD HH:mm:ss') || null,
      };

      if (isCreating) {
        await create(dataToSend);
        showMessage('Created successfully.', 'success');
      } else {
        if (id) {
          await update(id, dataToSend);
          showMessage('Updated successfully.', 'success');
        } else {
          showMessage('Error: No work order ID provided for update.', 'error');
          return;
        }
      }
      setEditing(false);
      navigate('/home/work-order');
    } catch (error) {
      showMessage('Failed to save.', 'error');
    }
  };

  const handleCancel = () => {
    setEditing(false);
    if (isCreating) {
      navigate('/home/work-order');
    } else {
      reset();
    }
  };

  const handleDelete = async () => {
    try {
      if (id) {
        await deleteById(id);
        showMessage('Deleted successfully.', 'success');
        navigate('/home/work-order');
      }
    } catch (error) {
      showMessage('Failed to delete.', 'error');
    }
    setOpenConfirmDialog(false);
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '70vh' }}>
        <CircularProgress size={60} color="inherit" />
      </Box>
    );
  }

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Container maxWidth="xl">
        <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
          <Typography variant="h4">{isCreating ? 'Create Work order' : 'Work order'}</Typography>
          <Stack direction="row" spacing={2}>
            {editing || isCreating ? (
              <>
                <Button variant="contained" color="primary" onClick={handleSubmit(handleSave)}>
                  {isCreating ? 'Create' : 'Save'}
                </Button>
                <Button variant="outlined" color="secondary" onClick={handleCancel}>
                  Cancel
                </Button>
              </>
            ) : (
              <>
                <Button variant="contained" color="warning" onClick={() => setOpenConfirmDialog(true)}>
                  Delete
                </Button>
                <Button variant="contained" color="primary" onClick={handleEdit}>
                  Edit
                </Button>
                <Button variant="contained" color="inherit" onClick={() => navigate('/home/work-order')}>
                  Back to List
                </Button>
              </>
            )}
          </Stack>
        </Stack>

<Box
  component="form"
  sx={{
    p: 3,
    borderRadius: 3,
    boxShadow: '0px 6px 20px rgba(0,0,0,0.05)',
    backgroundColor: theme.palette.background.paper,
    display: 'flex',
    flexDirection: 'column',
    gap: 3,
  }}
>
  {/* Work Order Number */}
  <TextField
    label="Work Order Number"
    {...register('workOrderNumber')}
    fullWidth
    disabled={!editing && !isCreating}
  />

  {/* Branch + Technician */}
  <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
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

  {/* Equipment + Recipient */}
  {branchId && (
    <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
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
  )}

  {/* Details */}
  <TextField
    label="Service Details"
    {...register('serviceDetails')}
    multiline
    rows={3}
    fullWidth
    disabled={!editing && !isCreating}
  />
  <TextField
    label="Observations"
    {...register('observations')}
    multiline
    rows={2}
    fullWidth
    disabled={!editing && !isCreating}
  />

  {/* Dates */}
  <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
    <Controller
      name="startTime"
      control={control}
      render={({ field }) => (
        <DateTimePicker label="Start Time" {...field} disabled={!editing && !isCreating} />
      )}
    />
    <Controller
      name="endTime"
      control={control}
      render={({ field }) => (
        <DateTimePicker label="End Time" {...field} disabled={!editing && !isCreating} />
      )}
    />
  </Stack>

  {/* Signatures */}
  <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
    <ControlledSignaturePad label="Recipient signature" name="recipientSignatureBase64" control={control} />
    <ControlledSignaturePad label="Technician signature" name="technicianSignatureBase64" control={control} />
  </Stack>

  {/* Upload */}
  <ControlledDropzone
    name="profileImage"
    control={control}
    label="Drag or select your file."
  />
</Box>

     
        {/* Dialog confirmación */}
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
            <Button onClick={() => setOpenConfirmDialog(false)} color="inherit">
              Cancel
            </Button>
            <Button onClick={handleDelete} color="error" disabled={deleteConfirmationText !== 'delete'}>
              Delete
            </Button>
          </DialogActions>
        </Dialog>
      </Container>
    </LocalizationProvider>
  );
}