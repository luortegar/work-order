import React, { SyntheticEvent, useCallback, useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Button, TextField, Stack, Typography, Box, Dialog, DialogTitle, DialogContent, DialogActions, CircularProgress, Autocomplete, Grid } from '@mui/material';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';
import { create, view, update, deletePhoto, uploadPhoto, viewPhotos, deleteById } from 'src/api/inspectionVisitApi';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import dayjs from 'dayjs';
import SimpleDropzone from './PhotoDropzone';
import PhotoGallery from './PhotoGallery';
import { FileResponse } from 'src/api/types/commonTypes';

export default function InspectionVisitView() {
  const { clientId, branchId, inspectionVisitId } = useParams();
  const navigate = useNavigate();
  const { showMessage } = useSnackbar();

  interface InspectionVisitData {
    inspectionVisitId: string;
    title: string;
    descriptions: string;
    date: string | null;
    branchId?: string;
  }

  const [data, setData] = useState<InspectionVisitData>({
    inspectionVisitId: '',
    title: '',
    descriptions: '',
    date: null, // Puede ser string o null según la interfaz
    branchId: branchId
  });
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [creating] = useState(inspectionVisitId === 'new');
  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
  const [deleteConfirmationText, setDeleteConfirmationText] = useState('');

  const [photoList, setPhotoList] = useState<FileResponse[]>([])


  useEffect(() => {
    if (creating) {
      setLoading(false);
      return;
    }

    const fetchData = async () => {
      try {
        if (inspectionVisitId) {
          const response = await view(inspectionVisitId);
          setData(response.data);
          setLoading(false);
        }
      } catch (error) {
        showMessage('Failed to load equipment data', 'error');
        setLoading(false);
      }
    };

    fetchData();
    refreshPhotoIdList()

  }, [inspectionVisitId, creating, showMessage]);



  const handleEdit = () => {
    setEditing(true);
  };

  const handleSave = async () => {
    try {
      if (creating) {
        await create(data);
        showMessage('Created successfully.', 'success');
      } else {
        if (inspectionVisitId) {
          console.log("save data______", data)
          await update(inspectionVisitId, data);
          showMessage('Updated successfully.', 'success');
        }
      }
      setEditing(false);
      navigate(`/home/client/${clientId}/branch/${branchId}/inspectionVisit`);
    } catch (error) {
      showMessage('Failed to save.', 'error');
    }
  };

  const handleCancel = () => {
    setEditing(false);
    if (creating) {
      navigate(`/home/client/${clientId}/branch/${branchId}/inspectionVisit`);
    } else {
      setData(data); // Reset to original values
    }
  };

  const handleDelete = async () => {
    try {
      if (inspectionVisitId) {
        await deleteById(inspectionVisitId);
        showMessage('Deleted successfully.', 'success');
        navigate(`/home/client/${clientId}/branch/${branchId}/inspectionVisit`);
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

  const handleConfirmTextChange = (event: any) => {
    setDeleteConfirmationText(event.target.value);
  };

  const handleConfirmDelete = () => {
    if (deleteConfirmationText === 'delete') {
      handleDelete();
    } else {
      showMessage('Please type "delete" to confirm', 'error');
    }
  };

  const refreshPhotoIdList = () => {
    viewPhotos(inspectionVisitId!).then((resp) => {
      setPhotoList(resp)
    })
  }

  const handleChange = (event: any) => {

    console.log(event)
    const { name, value } = event.target;
    setData(prevData => ({
      ...prevData,
      [name]: value
    }));
  };

  const handleDateChange = (newDate: any) => {
    // Convierte el objeto dayjs a string con el formato esperado
    const formattedDate = newDate ? dayjs(newDate).format('YYYY-MM-DD HH:mm:ss') : null;
    setData(prevData => ({
      ...prevData,
      date: formattedDate
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
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Container maxWidth="xl">
        <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
          <Typography variant="h4">{creating ? 'Create Inspection Visit' : 'Inspection Visit'}</Typography>
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
                <Button variant="contained" color="inherit" onClick={() => navigate(`/home/client/${clientId}/branch/${branchId}/inspectionVisit`)}>
                  Back to List
                </Button>
              </>
            )}
          </Stack>
        </Stack>

        <Box component="form" sx={{ display: 'flex', flexDirection: 'column', gap: 2, minWidth: 300 }}>

          <TextField
            label="Title"
            name="title"
            value={data.title}
            onChange={handleChange}
            variant="outlined"
            fullWidth
            disabled={!editing && !creating}
          />

          <TextField
            label="Descriptions"
            name="descriptions"
            value={data.descriptions}
            onChange={handleChange}
            variant="outlined"
            fullWidth
            multiline
            rows={4}
            disabled={!editing && !creating}
          />

          <DateTimePicker
            label="Date"
            name="date"
            onChange={handleDateChange}
            ampm={false}
            value={data.date ? dayjs(data.date) : null}
            disabled={!editing && !creating}
          />

          <Grid size={12}>
            {photoList &&
              <PhotoGallery id={inspectionVisitId!} photoList={photoList} deletePhoto={deletePhoto} refreshPhotos={refreshPhotoIdList} />
            }
          </Grid>
          {(editing || creating) && (
            <Grid size={12}>
              <SimpleDropzone uploadFile={uploadPhoto} refreshScreen={refreshPhotoIdList} workOrderId={inspectionVisitId!} label="Drag or select your file." />
            </Grid>
          )}

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
    </LocalizationProvider>
  );
}
