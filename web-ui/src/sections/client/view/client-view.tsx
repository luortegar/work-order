import React, { useEffect, useState, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Container, Button, TextField, Stack, Typography, Box,
  Dialog, DialogTitle, DialogContent, DialogActions, CircularProgress, Autocomplete,
  Card,
  useTheme
} from '@mui/material';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';
import { view, update, create, deleteById } from 'src/api/clientApi';
import { autocompleteTypeOfPurchase, TypeOfPurchaseResponse } from 'src/api/typeOfPurchaseApi';
import { autocompleteCommune, CommuneResponse } from 'src/api/addressApi';
import { ClientRequest, ClientResponse } from 'src/api/types/clientTypes';

const initialClient: ClientRequest = {
  clientId: '',
  companyName: '',
  uniqueTaxpayerIdentification: '',
  business: '',
  address: '',
  commune: '',
  city: '',
  typeOfPurchase: ''
};

export default function ClientView() {
  const { clientId } = useParams<{ clientId: string }>();
  const navigate = useNavigate();
  const { showMessage } = useSnackbar();
  const theme = useTheme();

  const creating = clientId === 'new';
  const [data, setData] = useState<ClientResponse>(initialClient);
  const [loading, setLoading] = useState(true);
  const [isEditable, setIsEditable] = useState(creating);
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [confirmText, setConfirmText] = useState('');

  const [typeOfPurchaseOptions, setTypeOfPurchaseOptions] = useState<TypeOfPurchaseResponse[]>([]);
  const [communeOptions, setCommuneOptions] = useState<CommuneResponse[]>([]);

  // ✅ Manejo de errores (solo companyName por ahora)
  const [errors, setErrors] = useState<{ companyName: boolean }>({ companyName: false });

  // Fetch client data
  useEffect(() => {
    if (creating) return setLoading(false);
    if (!clientId) return;
    view(clientId)
      .then(res => setData(res))
      .catch(() => showMessage('Failed to load client data', 'error'))
      .finally(() => setLoading(false));
  }, [clientId, creating, showMessage]);

  // Fetch type of purchase options
  useEffect(() => {
    autocompleteTypeOfPurchase('')
      .then(res => setTypeOfPurchaseOptions(res.data))
      .catch(() => showMessage('Failed to load type of purchase options', 'error'));
  }, [showMessage]);

  const fetchCommuneOptions = useCallback((input: string) => {
    autocompleteCommune(input)
      .then(res => setCommuneOptions(res.data))
      .catch(() => showMessage('Failed to load commune options', 'error'));
  }, [showMessage]);

  const handleChange = <K extends keyof ClientRequest>(name: K, value: ClientRequest[K]) => {
    setData(prev => ({ ...prev, [name]: value }));
    // Resetear error al escribir
    if (name === "companyName" && value.trim()) {
      setErrors(prev => ({ ...prev, companyName: false }));
    }
  };

  const handleSave = async () => {
    // ✅ Validación de campos obligatorios
    const newErrors = {
      companyName: !data.companyName.trim(),
    };
    setErrors(newErrors);

    if (Object.values(newErrors).some(Boolean)) {
      showMessage("Please fill all required fields", "error");
      return;
    }

    try {
      if (creating) {
        await create(data);
      } else if (clientId) {
        await update(clientId, data);
      }
      showMessage(`${creating ? 'Created' : 'Updated'} successfully.`, 'success');
      navigate('/home/client');
    } catch {
      showMessage('Failed to save.', 'error');
    }
  };

  const handleDelete = async () => {
    try {
      if (clientId) {
        await deleteById(clientId);
        showMessage('Deleted successfully.', 'success');
        navigate('/home/client');
      }
    } catch {
      showMessage('Failed to delete.', 'error');
    } finally {
      setConfirmOpen(false);
      setConfirmText('');
    }
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="70vh">
        <CircularProgress size={60} />
      </Box>
    );
  }

  return (
    <Container maxWidth="xl">
      {/* Header Buttons */}
      <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
        <Typography variant="h4">{creating ? 'Create Client' : 'Client'}</Typography>
        <Stack direction="row" spacing={2}>
          {isEditable ? (
            <>
              <Button variant="contained" color='primary' onClick={handleSave}>{creating ? 'Create' : 'Save'}</Button>
              <Button variant="contained" color='secondary' onClick={() => setIsEditable(false)}>Cancel</Button>
            </>
          ) : (
            <>
              <Button variant="contained" color="error" onClick={() => setConfirmOpen(true)}>Delete</Button>
              <Button variant="contained" color='primary' onClick={() => setIsEditable(true)}>Edit</Button>
              <Button variant="contained" color="inherit" onClick={() => navigate('/home/client')}>Back to List</Button>
            </>
          )}
        </Stack>
      </Stack>

      {/* Form */}
      <Card
        sx={{
          p: 2,
          borderRadius: 3,
          boxShadow: '0px 4px 20px rgba(0,0,0,0.05)',
          backgroundColor: theme.palette.background.paper,
        }}
      >
        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
          <TextField
            label="Company Name *"
            name="companyName"
            value={data.companyName}
            onChange={(e) => handleChange('companyName', e.target.value)}
            disabled={!isEditable}
            fullWidth
            error={errors.companyName}
            helperText={errors.companyName && 'Company Name is required'}
          />

          <TextField
            label="Unique Taxpayer Identification"
            name="uniqueTaxpayerIdentification"
            value={data.uniqueTaxpayerIdentification}
            onChange={(e) => handleChange('uniqueTaxpayerIdentification', e.target.value)}
            disabled={!isEditable}
            fullWidth
          />

          <TextField
            label="Business"
            name="business"
            value={data.business}
            onChange={(e) => handleChange('business', e.target.value)}
            disabled={!isEditable}
            fullWidth
          />

          <TextField
            label="Address"
            name="address"
            value={data.address}
            onChange={(e) => handleChange('address', e.target.value)}
            disabled={!isEditable}
            fullWidth
          />

          <TextField
            label="City"
            name="city"
            value={data.city}
            onChange={(e) => handleChange('city', e.target.value)}
            disabled={!isEditable}
            fullWidth
          />

          <Autocomplete
            options={communeOptions}
            getOptionLabel={(o: CommuneResponse | string) =>
              typeof o === 'string' ? o : o.commune
            }
            value={communeOptions.find(o => o.commune === data.commune) || data.commune}
            onChange={(_, v) =>
              handleChange('commune', typeof v === 'string' ? v : v?.commune || '')
            }
            onInputChange={(_, val) => fetchCommuneOptions(val)}
            freeSolo
            disabled={!isEditable}
            renderInput={(params) => <TextField {...params} label="Commune" fullWidth />}
          />

          <Autocomplete
            options={typeOfPurchaseOptions}
            getOptionLabel={(o: TypeOfPurchaseResponse) => o.name}
            value={typeOfPurchaseOptions.find(o => o.code === data.typeOfPurchase) || null}
            onChange={(_, v) => handleChange('typeOfPurchase', v?.code || '')}
            disabled={!isEditable}
            renderInput={(params) => <TextField {...params} label="Type of Purchase" fullWidth />}
          />
        </Box>
      </Card>

      {/* Confirm Dialog */}
      <Dialog open={confirmOpen} onClose={() => setConfirmOpen(false)} maxWidth="xs" fullWidth>
        <DialogTitle>Confirm Deletion</DialogTitle>
        <DialogContent>
          <Typography>Type "delete" to confirm deletion:</Typography>
          <TextField
            autoFocus
            margin="dense"
            fullWidth
            value={confirmText}
            onChange={(e) => setConfirmText(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setConfirmOpen(false)}>Cancel</Button>
          <Button
            onClick={() =>
              confirmText === 'delete'
                ? handleDelete()
                : showMessage('Please type "delete" to confirm', 'error')
            }
            color="error"
          >
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}
