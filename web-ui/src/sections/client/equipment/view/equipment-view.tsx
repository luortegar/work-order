import React, { SyntheticEvent, useCallback, useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Button, TextField, Stack, Typography, Box, Dialog, DialogTitle, DialogContent, DialogActions, CircularProgress, Autocomplete } from '@mui/material';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';
import { viewEquipment, updateEquipment, createEquipment, deleteEquipmentById, autocompleteEquipmentType } from 'src/api/equipmentApi';
import { EquipmentTypeResponse } from 'src/api/types/equitmentTypes';

export default function EquipmentView() {
  const {clientId, branchId, equipmentId } = useParams();
  const navigate = useNavigate();
  const { showMessage } = useSnackbar();

  const [data, setData] = useState({
    equipmentId: '',
    equipmentModel: '',
    equipmentBrand: '',
    serialNumber: '',
    equipmentTypeId:'',
    equipmentType: '',
    branchId: branchId
  });
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [creating] = useState(equipmentId === 'new');
  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
  const [deleteConfirmationText, setDeleteConfirmationText] = useState('');
  const [equipmentTypeOptions, setEquipmentTypeOptions] = useState<EquipmentTypeResponse[]>([]);

  useEffect(() => {
    if (creating) {
      setLoading(false);
      return;
    }

    const fetchData = async () => {
      try {
        if(equipmentId){
        const response = await viewEquipment(equipmentId);
        setData(response.data);
        setLoading(false);
        }
      } catch (error) {
        showMessage('Failed to load equipment data', 'error');
        setLoading(false);
      }
    };

    fetchData();
  }, [equipmentId, creating, showMessage]);

    const fetchEquipmentTypeOptions = useCallback((input: string) => {
      autocompleteEquipmentType(input)
        .then(res => setEquipmentTypeOptions(res))
        .catch(() => showMessage('Failed to load equipment type options', 'error'));
    }, [showMessage]);

  const handleEdit = () => {
    setEditing(true);
  };

  const handleSave = async () => {
    try {
      if (creating) {
        await createEquipment(data);
        showMessage('Created successfully.', 'success');
      } else {
        if(equipmentId){
          console.log("save data______",data)
          await updateEquipment(equipmentId, data);
          showMessage('Updated successfully.', 'success');
        }
      }
      setEditing(false);
      navigate(`/home/client/${clientId}/branch/${branchId}/equipment`);
    } catch (error) {
      showMessage('Failed to save.', 'error');
    }
  };

  const handleCancel = () => {
    setEditing(false);
    if (creating) {
      navigate(`/home/client/${clientId}/branch/${branchId}/equipment`);
    } else {
      setData(data); // Reset to original values
    }
  };

  const handleDelete = async () => {
    try {
      if(equipmentId){
        await deleteEquipmentById(equipmentId);
        showMessage('Deleted successfully.', 'success');
        navigate(`/home/client/${clientId}/branch/${branchId}/equipment`);
      }
    } catch (error) {
      showMessage('Failed to delete.', 'error');
    }
    setOpenConfirmDialog(false);
  };

  const handleOpenConfirmDialog = () => {
    setOpenConfirmDialog(true);
  };

  const handleCloseConfirmDialog = () => {
    setOpenConfirmDialog(false);
    setDeleteConfirmationText('');
  };

  const handleConfirmTextChange = (event:any) => {
    setDeleteConfirmationText(event.target.value);
  };

  const handleConfirmDelete = () => {
    if (deleteConfirmationText === 'delete') {
      handleDelete();
    } else {
      showMessage('Please type "delete" to confirm', 'error');
    }
  };

  const handleChange = (event:any) => {
    const { name, value } = event.target;
    setData(prevData => ({
      ...prevData,
      [name]: value
    }));
  };

  if (loading) {
    return (
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          height: '70vh'
        }}
      >
        <CircularProgress size={60} color="inherit" />
      </Box>
    );
  }

  const handleEquipmentTypeChange = (event: any, newValue: any) => {
    setData(prevData => ({
      ...prevData,
      equipmentTypeId: newValue ? newValue.equipmentTypeId || newValue : ''
    }));
  };

  return (
    <Container maxWidth="xl">
      <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
        <Typography variant="h4">{creating ? 'Create Equipment' : 'Equipment'}</Typography>
        <Stack direction="row" spacing={2}>
          {editing || creating ? (
            <>
              <Button variant="contained" color="primary" onClick={handleSave}>
                {creating ? 'Create' : 'Save'}
              </Button>
              <Button variant="contained" color="secondary" onClick={handleCancel}>
                Cancel
              </Button>
            </>
          ) : (
            <>
              <Button variant="contained" color="error" onClick={handleOpenConfirmDialog}>
                Delete
              </Button>
              <Button variant="contained" color="primary" onClick={handleEdit}>
                Edit
              </Button>
              <Button variant="contained" color="inherit" onClick={() => navigate(`/home/client/${clientId}/branch/${branchId}/equipment`)}>
                Back to List
              </Button>
            </>
          )}
        </Stack>
      </Stack>

      <Box component="form" sx={{ display: 'flex', flexDirection: 'column', gap: 2, minWidth: 300 }}>
        <TextField
          label="Equipment Model"
          name="equipmentModel"
          value={data.equipmentModel}
          onChange={handleChange}
          variant="outlined"
          fullWidth
          disabled={!editing && !creating}
        />
        <TextField
          label="Equipment Brand"
          name="equipmentBrand"
          value={data.equipmentBrand}
          onChange={handleChange}
          variant="outlined"
          fullWidth
          disabled={!editing && !creating}
        />
        <TextField
          label="Serial Number"
          name="serialNumber"
          value={data.serialNumber}
          onChange={handleChange}
          variant="outlined"
          fullWidth
          disabled={!editing && !creating}
        />
        <Autocomplete
          options={equipmentTypeOptions}
          getOptionLabel={(option) =>
            typeof option === 'string' ? option : option.typeName
          }
          value={equipmentTypeOptions.find(option => option.equipmentTypeId == data.equipmentTypeId) || data.equipmentType}
          onChange={handleEquipmentTypeChange}
          onInputChange={(event, newInputValue) => fetchEquipmentTypeOptions(newInputValue)}
          freeSolo
          disabled={!editing && !creating}
          renderInput={(params) => (
            <TextField
              {...params}
              label="Equipment type"
              variant="outlined"
              fullWidth
            />
          )}
        />

      </Box>

      <Dialog
        maxWidth="xs"
        fullWidth
        open={openConfirmDialog}
        onClose={handleCloseConfirmDialog}
      >
        <DialogTitle>Confirm Deletion</DialogTitle>
        <DialogContent>
          <Typography>Type "delete" to confirm deletion:</Typography>
          <TextField
            autoFocus
            margin="dense"
            label="Confirmation Text"
            fullWidth
            variant="outlined"
            value={deleteConfirmationText}
            onChange={handleConfirmTextChange}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseConfirmDialog}>Cancel</Button>
          <Button onClick={handleConfirmDelete} color="error">Delete</Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}
