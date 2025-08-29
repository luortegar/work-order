import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Button, TextField, Stack, Typography, Box, Dialog, DialogTitle, DialogContent, DialogActions, CircularProgress, Autocomplete } from '@mui/material';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';
import { viewUser, updateUser, createUser, deleteUser } from 'src/api/userCrudApi';
import { RoleAutocompleteResponse } from 'src/api/types/roleTypes';
import { autocompleteRole } from 'src/api/roleApi';

export default function UserView() {
  const { id } = useParams<{id:string}>();
  const navigate = useNavigate();
  const { showMessage } = useSnackbar();

  const [userData, setUserData] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [creating, setCreating] = useState(id === 'new');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [roleIds, setRoleIds] = useState<string[]>([]);
  const [password, setPassword] = useState(''); // State for password
  const [confirmPassword, setConfirmPassword] = useState(''); // State for confirming password
  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
  const [deleteConfirmationText, setDeleteConfirmationText] = useState('');

  const [roleOptions, setRoleOptions] = useState<RoleAutocompleteResponse[]>([]);
  

  useEffect(() => {
    if (creating) {
      setLoading(false);
      return;
    }

    const fetchUserData = async () => {
      try {
        if(id){
          const response = await viewUser(id);
          setUserData(response.data);
          setFirstName(response.data.firstName);
          setLastName(response.data.lastName);
          setEmail(response.data.email);
          setRoleIds(response.data.roleIds)
          setLoading(false);
        }
      } catch (error) {
        showMessage('Failed to load user data', 'error');
        setLoading(false);
      }
    };

    fetchUserData();
  }, [id, creating, showMessage]);

    useEffect(() => {
      autocompleteRole()
        .then(res => {setRoleOptions(res); console.log("roles__", res)})
        .catch(() => showMessage('Failed to load roles', 'error'));
    }, [showMessage]);

  const handleEdit = () => {
    setEditing(true);
  };

  const handleSave = async () => {
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
          password,
          roleIds
        };
        await createUser(newUser);
        showMessage('User created successfully', 'success');
        navigate('/home/user');
      } else {
        const updatedUser = {
          firstName,
          lastName,
          email,
          roleIds,
          ...(password && { password }) // Include password only if it's provided
        };
        if(id){
          await updateUser(id, updatedUser);
          setUserData({ ...userData, firstName, lastName, email, roleIds});
          showMessage('User updated successfully', 'success');
        }
      }
      setEditing(false);
    } catch (error) {
      showMessage('Failed to save user', 'error');
    }
  };

  const handleCancel = () => {
    setEditing(false);
    if (creating) {
      navigate('/home/user');
    } 
    else {
      if(userData){
      setFirstName(userData?.firstName);
      setLastName(userData?.lastName);
      setEmail(userData?.email);
      setPassword('');
      setConfirmPassword('');
      }
    }
  };

  const handleDelete = async () => {
    try {
      if(id){
        await deleteUser(id);
        showMessage('User deleted successfully', 'success');
        navigate('/home/user');
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
        <CircularProgress size={60} color="inherit"/>
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
              <Button variant="contained" color="inherit" onClick={handleCancel}>
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
              <Button variant="contained" color="inherit" onClick={() => navigate('/home/user')}>
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
          onChange={(e) => setEmail(e.target.value)}
          fullWidth
        />
        {roleOptions && (
          <Autocomplete
            multiple
            options={roleOptions}
            getOptionLabel={(o: RoleAutocompleteResponse) => o.roleFullName}
            value={roleOptions.filter(o => roleIds.includes(o.roleId))} 
            onChange={(_, values) => setRoleIds(values.map(v => v.roleId))} 
            disabled={!editing && !creating}
            renderInput={(params) => (
              <TextField {...params} label="Roles" fullWidth />
            )}
          />
        )}

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
        {creating && (
          <>
            <TextField
              label="Password"
              type="password"
              name="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              fullWidth
            />
            <TextField
              label="Confirm Password"
              type="password"
              name="confirmPassword"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              fullWidth
            />
          </>
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
          <Button onClick={handleConfirmDelete} color="error">Delete</Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}
