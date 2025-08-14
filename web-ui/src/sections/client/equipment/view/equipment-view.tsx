import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Button, TextField, Stack, Typography, Box, Dialog, DialogTitle, DialogContent, DialogActions, CircularProgress } from '@mui/material';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';
import { viewEquipment, updateEquipment, createEquipment, deleteEquipmentById } from 'src/api/equipmentApi';

export default function EquipmentView() {
  const {clientId, branchId, equipmentId } = useParams();
  const navigate = useNavigate();
  const { showMessage } = useSnackbar();

  const [data, setData] = useState({
    equipmentId: '',
    equipmentModel: '',
    equipmentBrand: '',
    serialNumber: '',
    equipmentType: ''
  });
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [creating] = useState(equipmentId === 'new');
  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
  const [deleteConfirmationText, setDeleteConfirmationText] = useState('');

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
              <Button variant="outlined" color="secondary" onClick={handleCancel}>
                Cancel
              </Button>
            </>
          ) : (
            <>
              <Button variant="outlined" color="error" onClick={handleOpenConfirmDialog}>
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
        <TextField
          label="Equipment Type"
          name="equipmentType"
          value={data.equipmentType}
          onChange={handleChange}
          variant="outlined"
          fullWidth
          disabled={!editing && !creating}
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
