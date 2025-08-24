import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
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
} from '@mui/material';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';
import { viewUser, updateUser, createUserClient, deleteUser } from 'src/api/userCrudApi';

export default function UserView() {
  const { userId, clientId } = useParams();
  const navigate = useNavigate();
  const { showMessage } = useSnackbar();

  const [userData, setUserData] = useState<{ firstName: string; lastName: string; email: string }>();
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [creating, setCreating] = useState(userId === 'new');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
  const [deleteConfirmationText, setDeleteConfirmationText] = useState('');

  const [emailError, setEmailError] = useState('');

  useEffect(() => {
    if (creating) {
      setLoading(false);
      return;
    }

    const fetchUserData = async () => {
      try {
        if (userId) {
          const response = await viewUser(userId);
          setUserData(response.data);
          setFirstName(response.data.firstName);
          setLastName(response.data.lastName);
          setEmail(response.data.email);
          setLoading(false);
        }
      } catch (error) {
        showMessage('Failed to load user data', 'error');
        setLoading(false);
      }
    };

    fetchUserData();
  }, [userId, creating, showMessage]);

  const validateEmail = (value: string) => {
    if (!value.trim()) return 'Email is required';
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!regex.test(value)) return 'Invalid email format';
    return '';
  };

  const handleEdit = () => {
    setEditing(true);
  };

  const handleSave = async () => {
    const error = validateEmail(email);
    setEmailError(error);
    if (error) {
      showMessage(error, 'error');
      return;
    }

    try {
      if (creating) {
        if (password !== confirmPassword) {
          showMessage('Passwords do not match', 'error');
          return;
        }
        const newUser = {
          firstName,
          lastName,
          email,
          clientId,
        };
        await createUserClient(newUser);
        showMessage('User created successfully', 'success');
        navigate(`/home/client/${clientId}/employees`);
      } else {
        if (userId && userData !== null) {
          const updatedUser = {
            firstName,
            lastName,
            email,
            ...(password && { password }),
          };
          await updateUser(userId, updatedUser);
          setUserData({ ...userData, firstName, lastName, email });
          showMessage('User updated successfully', 'success');
        }
      }
      setEditing(false);
    } catch (error: any) {
      showMessage(
        error.response?.data?.message ?? 'Failed to save user',
        'error'
      );
    }
  };

  const handleCancel = () => {
    setEditing(false);
    if (creating) {
      navigate(`/home/client/${clientId}/employees`);
    } else {
      if (userData) {
        setFirstName(userData.firstName);
        setLastName(userData.lastName);
        setEmail(userData.email);
        setPassword('');
        setConfirmPassword('');
      }
    }
  };

  const handleDelete = async () => {
    try {
      if (userId) {
        await deleteUser(userId);
        showMessage('User deleted successfully', 'success');
        navigate(`/home/client/${clientId}/employees`);
      }
    } catch (error) {
      showMessage('Failed to delete user', 'error');
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
    <Container maxWidth="xl">
      <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
        <Typography variant="h4">{creating ? 'Create User' : 'User'}</Typography>
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
                Delete User
              </Button>
              <Button variant="contained" color="primary" onClick={handleEdit}>
                Edit User
              </Button>
              <Button
                variant="contained"
                color="inherit"
                onClick={() => navigate(`/home/client/${clientId}/employees`)}
              >
                Back to List
              </Button>
            </>
          )}
        </Stack>
      </Stack>

      <Box component="form" sx={{ display: 'flex', flexDirection: 'column', gap: 2, minWidth: 300 }}>
        <TextField
          label="First Name"
          name="firstName"
          disabled={!editing && !creating}
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
          fullWidth
        />
        <TextField
          label="Last Name"
          name="lastName"
          disabled={!editing && !creating}
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
          fullWidth
        />
        <TextField
          label="Email"
          name="email"
          disabled={!editing && !creating}
          value={email}
          onChange={(e) => {
            setEmail(e.target.value);
            if (editing || creating) {
              setEmailError(validateEmail(e.target.value));
            }
          }}
          required
          fullWidth
          error={!!emailError && (editing || creating)}
          helperText={emailError && (editing || creating) ? emailError : ''}
        />
        {editing && !creating && (
          <TextField
            label="Password"
            type="password"
            name="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            fullWidth
          />
        )}
      </Box>

      {/* Confirmation Dialog */}
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
          <Button onClick={handleConfirmDelete} color="error">
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}
