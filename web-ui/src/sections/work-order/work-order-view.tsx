import React, { use, useCallback, useEffect, useState } from 'react';
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
  Card,
  useTheme,
} from '@mui/material';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';
import { view, update, create, deleteById } from 'src/api/workOrderApi';
import Autocomplete from '@mui/material/Autocomplete';
import { autocompleteBranch } from 'src/api/branchApi';
import { autocompleteEquipmentAndFilterByBranchId } from 'src/api/equipmentApi';
import { BranchAutocompleteResponse } from 'src/api/types/branchTypes';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import dayjs from 'dayjs';
import { EquipmentAutocompleteResponse } from 'src/api/types/equitmentTypes';
import { autocompleteEmployeesAndFilterByBranchId } from 'src/api/employeesApi';
import { EmployeesAutocompleteResponse } from 'src/api/types/employeesTypes';
import { useAuth } from 'src/context/auth/AuthContext';

// ----------------------
// Tipos
// ----------------------
interface WorkOrderForm {
  workOrderId: string;
  workOrderNumber: string;
  clientId: string;
  branchId: string;
  recipientId: string;
  technicianId: string;
  equipmentId: string;
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

  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [creating] = useState(id === 'new');
  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
  const [deleteConfirmationText, setDeleteConfirmationText] = useState('');
  const [brantOptions, setBrantOptions] = useState<BranchAutocompleteResponse[]>([]);
  const [equipmentOptions, setEquipmentOptions] = useState<EquipmentAutocompleteResponse[]>([]);
  const [employeesOptions, setEmployeesOptions] = useState<EmployeesAutocompleteResponse[]>([]);

    const { userId } = useAuth();
  

  // useForm tipado
  const { register, handleSubmit, reset, control, getValues, watch } = useForm<WorkOrderForm>({
    defaultValues: {
      workOrderId: '',
      workOrderNumber: '',
      clientId: '',
      branchId: '',
      recipientId: '',
      technicianId: '',
      equipmentId: '',
      serviceDetails: '',
      observations: '',
      photoIdList: [],
      startTime: null,
      endTime: null,
    },
  });

  const branchId = watch('branchId');


  const fetchBranchOptions = useCallback(
    (input: string) => {
      autocompleteBranch(input)
        .then((res) => setBrantOptions(res))
        .catch(() => showMessage('Failed to load branch options', 'error'));
    },
    [showMessage]
  );

  const fetchEquipmentOptions = useCallback(
    (branchId: string, input: string) => {
      autocompleteEquipmentAndFilterByBranchId(branchId, input)
        .then((res) => setEquipmentOptions(res))
        .catch(() => showMessage('Failed to load branch options', 'error'));
    },
    [showMessage]
  );

  const fetchEmployeesOptions = useCallback(
    (branchId: string, input: string) => {
      autocompleteEmployeesAndFilterByBranchId(branchId, input)
        .then((res) => setEmployeesOptions(res))
        .catch(() => showMessage('Failed to load employees options', 'error'));
    },
    [showMessage]
  );



//autocompleteBranch

  useEffect(() => {

  }, [])

  useEffect(() => {
    if (creating) {
      setLoading(false);
      return;
    }

    const fetchData = async () => {
      if (id) {
        try {
          const response = await view(id);

          if (response.branchId) {
            const branchResult = await autocompleteBranch('');
            setBrantOptions(branchResult);

            // Si el branch no está en la lista, agregarlo
            if (!branchResult.find(b => b.branchId === response.branchId)) {
              const currentBranch = await autocompleteBranch(response.branchId);
              setBrantOptions(prev => [...prev, ...currentBranch]);
            }

            // 2. Cargar EquipmentOptions si existe branchId y equipmentId
            if (response.equipmentId) {
              const equipmentResult = await autocompleteEquipmentAndFilterByBranchId(response.branchId, '');
              setEquipmentOptions(equipmentResult);

              if (!equipmentResult.find(e => e.equipmentId === response.equipmentId)) {
                const currentEquipment = await autocompleteEquipmentAndFilterByBranchId(response.branchId, response.equipmentId);
                setEquipmentOptions(prev => [...prev, ...currentEquipment]);
              }
            }
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
  }, [id, creating, showMessage, reset]);

  const handleEdit = () => {
    setEditing(true);
  };

  const handleSave = async (formData: WorkOrderForm) => {
    try {
      const dataToSend = {
        ...formData,
        startTime: formData.startTime ? formData.startTime.format('YYYY-MM-DD HH:mm:ss') : null,
        endTime: formData.endTime ? formData.endTime.format('YYYY-MM-DD HH:mm:ss') : null,
      };

      if (creating) {
        await create(dataToSend);
        showMessage('Created successfully.', 'success');
      } else if (id) {
        await update(id, dataToSend);
        showMessage('Updated successfully.', 'success');
      }
      setEditing(false);
      navigate('/home/work-order');
    } catch (error) {
      showMessage('Failed to save.', 'error');
    }
  };

  const handleCancel = () => {
    setEditing(false);
    if (creating) {
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

  const handleOpenConfirmDialog = () => setOpenConfirmDialog(true);
  const handleCloseConfirmDialog = () => {
    setOpenConfirmDialog(false);
    setDeleteConfirmationText('');
  };
  const handleConfirmTextChange = (event: React.ChangeEvent<HTMLInputElement>) =>
    setDeleteConfirmationText(event.target.value);
  const handleConfirmDelete = () => {
    if (deleteConfirmationText === 'delete') {
      handleDelete();
    } else {
      showMessage('Please type "delete" to confirm', 'error');
    }
  };

  if (loading) {
    return (
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          height: '70vh',
        }}
      >
        <CircularProgress size={60} color="inherit" />
      </Box>
    );
  }

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Container maxWidth="xl">
        <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
          <Typography variant="h4">{creating ? 'Create Work order' : 'Work order'}</Typography>
          <Stack direction="row" spacing={2}>
            {editing || creating ? (
              <>
                <Button variant="contained" color="primary" onClick={handleSubmit(handleSave)}>
                  {creating ? 'Create' : 'Save'}
                </Button>
                <Button variant="outlined" color="secondary" onClick={handleCancel}>
                  Cancel
                </Button>
              </>
            ) : (
              <>
                <Button variant="contained" color="warning" onClick={handleOpenConfirmDialog}>
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
        <Card
          sx={{
            p: 2,
            borderRadius: 3,
            boxShadow: '0px 4px 20px rgba(0,0,0,0.05)',
            backgroundColor: theme.palette.background.paper,
          }}
        >
          <Box component="form" sx={{ display: 'flex', flexDirection: 'column', gap: 2, minWidth: 300 }}>
            <TextField
              label="Work Order Number"
              {...register('workOrderNumber')}
              variant="outlined"
              fullWidth
              disabled={!editing && !creating}
            />

            <Controller
              name="branchId"
              control={control}
              render={({ field }) => (
                <Autocomplete<BranchAutocompleteResponse, false, false, true>
                  options={brantOptions}
                  getOptionLabel={(option) =>
                    typeof option === 'string' ? option : option.branchFullName
                  }

                  onChange={(event, newValue) => {
                    if (newValue && typeof newValue !== 'string') {
                      field.onChange(newValue.branchId);
                    } else {
                      field.onChange('');
                    }
                  }}
                  onInputChange={(event, newInputValue) => fetchBranchOptions(newInputValue)}
                  value={
                    brantOptions.find((option) => option.branchId === getValues('branchId'))
                  }
                  freeSolo
                  disabled={!editing && !creating}
                  renderInput={(params) => (
                    <TextField {...params} label="Branch" variant="outlined" fullWidth />
                  )}
                />
              )}
            />
            {branchId &&
            <>
              <Controller
                name="equipmentId"
                control={control}
                render={({ field }) => (
                  <Autocomplete<EquipmentAutocompleteResponse, false, false, true>
                    options={equipmentOptions}
                    getOptionLabel={(option) =>
                      typeof option === 'string' ? option : option.equipmentFullName
                    }

                    onChange={(event, newValue) => {
                      if (newValue && typeof newValue !== 'string') {
                        field.onChange(newValue.equipmentId);
                      } else {
                        field.onChange('');
                      }
                    }}
                    onInputChange={(event, newInputValue) => {
                      console.log("_____branchId_____", branchId)
                      fetchEquipmentOptions(branchId, newInputValue)
                    }
                    }
                    value={
                      equipmentOptions.find((option) => option.equipmentId === getValues('equipmentId')) || null
                    }
                    freeSolo
                    disabled={!editing && !creating}
                    renderInput={(params) => (
                      <TextField {...params} label="Equipment" variant="outlined" fullWidth />
                    )}
                  />
                )}
              />
                <Controller
                name="recipientId"
                control={control}
                render={({ field }) => (
                  <Autocomplete<EmployeesAutocompleteResponse, false, false, true>
                    options={employeesOptions}
                    getOptionLabel={(option) =>
                      typeof option === 'string' ? option : option.employeeFullName
                    }

                    onChange={(event, newValue) => {
                      if (newValue && typeof newValue !== 'string') {
                        field.onChange(newValue.employeeId);
                      } else {
                        field.onChange('');
                      }
                    }}
                    onInputChange={(event, newInputValue) => {
                      console.log("_____recipientId_____", branchId)
                      fetchEmployeesOptions(branchId, newInputValue)
                    }
                    }
                    value={
                      employeesOptions.find((option) => option.employeeId === getValues('recipientId')) || null
                    }
                    freeSolo
                    disabled={!editing && !creating}
                    renderInput={(params) => (
                      <TextField {...params} label="Recipient" variant="outlined" fullWidth />
                    )}
                  />
                )}
              />
              </>
            }

            <TextField
              label="Service Details"
              {...register('serviceDetails')}
              variant="outlined"
              fullWidth
              disabled={!editing && !creating}
            />
            <TextField
              label="Observations"
              {...register('observations')}
              variant="outlined"
              fullWidth
              disabled={!editing && !creating}
            />

            <Controller
              name="startTime"
              control={control}
              render={({ field }) => (
                <DateTimePicker label="Start Time" {...field} disabled={!editing && !creating} />
              )}
            />

            <Controller
              name="endTime"
              control={control}
              render={({ field }) => (
                <DateTimePicker label="End Time" {...field} disabled={!editing && !creating} />
              )}
            />
          </Box>
        </Card>

        {/* Dialog de confirmación */}
        <Dialog open={openConfirmDialog} onClose={handleCloseConfirmDialog}>
          <DialogTitle>Confirm Delete</DialogTitle>
          <DialogContent>
            <Typography>Type "delete" to confirm deletion:</Typography>
            <TextField
              value={deleteConfirmationText}
              onChange={handleConfirmTextChange}
              fullWidth
              margin="dense"
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseConfirmDialog} color="inherit">
              Cancel
            </Button>
            <Button onClick={handleConfirmDelete} color="error">
              Delete
            </Button>
          </DialogActions>
        </Dialog>
      </Container>
    </LocalizationProvider>
  );
}
