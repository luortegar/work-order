import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Button, TextField, Stack, Typography, Box, Dialog, DialogTitle, DialogContent, DialogActions, CircularProgress, Autocomplete } from '@mui/material';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';
import { viewBranch, updateBranch, createBranch, deleteBranchById } from 'src/api/branchApi';
import { autocompleteCommune, CommuneResponse } from 'src/api/addressApi';


export default function BranchView() {
  const {clientId, branchId } = useParams();
  const navigate = useNavigate();
  const { showMessage } = useSnackbar();

  const [data, setData] = useState({
    branchId: '',
    branchName: '',
    address: '',
    commune: '',
    region: '',
    clientId: clientId
  });
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [creating] = useState(branchId === 'new');
  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
  const [deleteConfirmationText, setDeleteConfirmationText] = useState('');
  const [communeOptions, setCommuneOptions] =  useState<CommuneResponse[]>([]);

  useEffect(() => {
    if (creating) {
      setLoading(false);
      return;
    }

    const fetchData = async () => {
      try {
        if(branchId){
          const response = await viewBranch(branchId);
          setData(response.data);
          setLoading(false);
        }
      } catch (error) {
        showMessage('Failed to load branch data', 'error');
        setLoading(false);
      }
    };

    fetchData();
  }, [branchId, creating, showMessage]);

  const fetchCommuneOptions = async (input:string) => {
    try {
      const response = await autocompleteCommune(input);
      setCommuneOptions(response.data);
    } catch (error) {
      showMessage('Failed to load commune options', 'error');
    }
  };

  const handleEdit = () => {
    setEditing(true);
  };

  const handleSave = async () => {
    try {
      if (creating) {
        await createBranch(data);
        showMessage('Created successfully.', 'success');
      } else {
        if(branchId){
        await updateBranch(branchId, data);
        showMessage('Updated successfully.', 'success');
        }
      }
      setEditing(false);
      navigate(`/home/client/${clientId}/branch`);
    } catch (error) {
      showMessage('Failed to save.', 'error');
    }
  };

  const handleCancel = () => {
    setEditing(false);
    if (creating) {
      navigate(`/home/client/${clientId}/branch`);
    } else {
      setData(data); // Reset to original values
    }
  };

  const handleDelete = async () => {
    try {
      if(branchId){
      await deleteBranchById(branchId);
      showMessage('Deleted successfully.', 'success');
      navigate(`/home/client/${clientId}/branch`);
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

  const handleCommuneChange = (event:any, newValue:any) => {
    setData(prevData => ({
      ...prevData,
      commune: newValue ? newValue.commune || newValue : ''
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
        <Typography variant="h4">{creating ? 'Create Branch' : 'Branch'}</Typography>
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
              <Button variant="contained" color="error" onClick={handleOpenConfirmDialog}>
                Delete
              </Button>
              <Button variant="contained" color="primary" onClick={handleEdit}>
                Edit
              </Button>
              <Button variant="contained" color="inherit" onClick={() => navigate(`/home/client/${clientId}/branch`)}>
                Back to List
              </Button>
            </>
          )}
        </Stack>
      </Stack>

      <Box component="form" sx={{ display: 'flex', flexDirection: 'column', gap: 2, minWidth: 300 }}>
        <TextField
          label="Branch Name"
          name="branchName"
          value={data.branchName}
          onChange={handleChange}
          variant="outlined"
          fullWidth
          disabled={!editing && !creating}
        />
        <TextField
          label="Address"
          name="address"
          value={data.address}
          onChange={handleChange}
          variant="outlined"
          fullWidth
          disabled={!editing && !creating}
        />
        <Autocomplete
          options={communeOptions}
            getOptionLabel={(option) =>
              typeof option === 'string' ? option : option.commune
            }
          value={communeOptions.find(option => option.commune === data.commune) || data.commune}
          onChange={handleCommuneChange}
          onInputChange={(event, newInputValue) => fetchCommuneOptions(newInputValue)}
          freeSolo
          disabled={!editing && !creating}
          renderInput={(params) => (
            <TextField
              {...params}
              label="Commune"
              variant="outlined"
              fullWidth
            />
          )}
        />
        <TextField
          label="Region"
          name="region"
          value={data.region}
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
