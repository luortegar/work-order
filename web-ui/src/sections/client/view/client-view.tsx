import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Button, TextField, Stack, Typography, Box, Dialog, DialogTitle, DialogContent, DialogActions, CircularProgress, Autocomplete } from '@mui/material';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';
import { view, update, create, deleteById } from 'src/api/clientApi';
import { autocompleteTypeOfPurchase, TypeOfPurchaseResponse } from 'src/api/typeOfPurchaseApi';
import { autocompleteCommune, CommuneResponse } from 'src/api/addressApi';

export default function ClientView() {
  const { clientId } = useParams();
  const navigate = useNavigate();
  const { showMessage } = useSnackbar();

  const [data, setData] = useState({
    clientId: '',
    companyName: '',
    uniqueTaxpayerIdentification: '',
    business: '',
    address: '',
    commune: '',
    city: '',
    typeOfPurchase: ''
  });
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [creating] = useState(clientId === 'new');
  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
  const [deleteConfirmationText, setDeleteConfirmationText] = useState('');
  const [typeOfPurchaseOptions, setTypeOfPurchaseOptions] = useState<TypeOfPurchaseResponse[]>([]);
  const [communeOptions, setCommuneOptions] = useState<CommuneResponse[]>([]);

  useEffect(() => {
    if (creating) {
      setLoading(false);
      return;
    }

    const fetchData = async () => {
      try {
        if(clientId){
          const response = await view(clientId);
          setData(response.data);
          setLoading(false);
        }
      } catch (error) {
        showMessage('Failed to load client data', 'error');
        setLoading(false);
      }
    };

    fetchData();
  }, [clientId, creating, showMessage]);

  useEffect(() => {
    const fetchTypeOfPurchaseOptions = async () => {
      try {
        const response = await autocompleteTypeOfPurchase('');
        setTypeOfPurchaseOptions(response.data);
      } catch (error) {
        showMessage('Failed to load type of purchase options', 'error');
      }
    };

    fetchTypeOfPurchaseOptions();
  }, [showMessage]);

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
        await create(data);
        showMessage('Created successfully.', 'success');
      } else {
        if(clientId){
          await update(clientId, data);
          showMessage('Updated successfully.', 'success');
        }
      }
      setEditing(false);
      navigate('/home/client');
    } catch (error) {
      showMessage('Failed to save.', 'error');
    }
  };

  const handleCancel = () => {
    setEditing(false);
    if (creating) {
      navigate('/home/client');
    } else {
      setData(data); // Reset to original values
    }
  };

  const handleDelete = async () => {
    try {
      if(clientId){
        await deleteById(clientId);
        showMessage('Deleted successfully.', 'success');
        navigate('/home/client');
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

  const handleTypeOfPurchaseChange = (event:any, newValue:any) => {
    setData(prevData => ({
      ...prevData,
      typeOfPurchase: newValue ? newValue.code : ''
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
        <Typography variant="h4">{creating ? 'Create Client' : 'Client'}</Typography>
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
              <Button variant="contained" color="inherit" onClick={() => navigate('/home/client')}>
                Back to List
              </Button>
            </>
          )}
        </Stack>
      </Stack>

      <Box component="form" sx={{ display: 'flex', flexDirection: 'column', gap: 2, minWidth: 300 }}>
        <TextField
          label="Company Name"
          name="companyName"
          value={data.companyName}
          onChange={handleChange}
          variant="outlined"
          fullWidth
          disabled={!editing && !creating}
        />
        <TextField
          label="Unique Taxpayer Identification"
          name="uniqueTaxpayerIdentification"
          value={data.uniqueTaxpayerIdentification}
          onChange={handleChange}
          variant="outlined"
          fullWidth
          disabled={!editing && !creating}
        />
        <TextField
          label="Business"
          name="business"
          value={data.business}
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
          label="City"
          name="city"
          value={data.city}
          onChange={handleChange}
          variant="outlined"
          fullWidth
          disabled={!editing && !creating}
        />
        <Autocomplete
          options={typeOfPurchaseOptions}
          getOptionLabel={(option) => option.name}
          value={typeOfPurchaseOptions.find(option => option.code === data.typeOfPurchase) || null}
          onChange={handleTypeOfPurchaseChange}
          disabled={!editing && !creating}
          renderInput={(params) => (
            <TextField
              {...params}
              label="Type of Purchase"
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
