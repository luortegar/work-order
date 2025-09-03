import React, { useState, useEffect, useContext } from 'react';
import {
  TextField, Button, Typography, Container, Avatar,
  Card, CardContent, CardActions, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle,
  InputAdornment, CardHeader,
  Grid
} from '@mui/material';
import { viewMyUser, updateUser, userUpdateParcial } from 'src/api/userCrudApi';
import { uploadFile } from "src/api/fileApi";
import { UserContext } from "../../../context/user/UserContext";


export default function ProfileView() {
  const [userId, setUserId] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [firstName, setFirstName] = useState<string>('');
  const [lastName, setLastName] = useState<string>('');
  const [avatar, setAvatar] = useState<string>('/assets/images/avatars/avatar_25.jpg');
  const [editMode, setEditMode] = useState<boolean>(false);
  const [changePasswordDialogOpen, setChangePasswordDialogOpen] = useState<boolean>(false);
  const [currentPassword, setCurrentPassword] = useState<string>('');
  const [newPassword, setNewPassword] = useState<string>('');
  const [confirmNewPassword, setConfirmNewPassword] = useState<string>('');
  const [showCurrentPassword, setShowCurrentPassword] = useState<boolean>(false);
  const [showNewPassword, setShowNewPassword] = useState<boolean>(false);
  const [showConfirmNewPassword, setShowConfirmNewPassword] = useState<boolean>(false);
  const {resetUser} = useContext(UserContext)!


  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const user = await viewMyUser();
        setUserId(user.data.userId);
        setEmail(user.data.email);
        setFirstName(user.data.firstName);
        setLastName(user.data.lastName);
        setAvatar(user.data.profilePictureUrl);
      } catch (error) {
        console.error("Error fetching user data:", error);
      }
    };

    fetchUserData();
  }, []);

  const handleAvatarChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      setAvatar(URL.createObjectURL(file));
      uploadFile(file).then((response) => {
        userUpdateParcial(userId, { profilePictureFileId: response.data.fileId }).then((resp) => {
          setAvatar(resp.data.profilePictureUrl);
          resetUser()
        });
      }).catch((error) => {
        console.error("Error uploading avatar:", error);
      });
    }
  };

  const handleSave = async () => {
    try {
      const payload = { email, firstName, lastName, avatarUrl: avatar };
      updateUser(userId, payload).then((resp)=>{
        setEditMode(false)
        resetUser()
      })
    } catch (error) {
      console.error("Error updating user data:", error);
    }
  };

  const handleChangePassword = () => {
    if (newPassword !== confirmNewPassword) {
      alert("Las contraseñas nuevas no coinciden.");
      return;
    }
    // Aquí agregas la lógica para cambiar la contraseña
    console.log("Contraseña actual:", currentPassword);
    console.log("Nueva contraseña:", newPassword);
    setChangePasswordDialogOpen(false);
  };

  return (
    <Container maxWidth="xl">
      <Typography variant="h4" sx={{ mb: 5 }}>
        Perfil de Usuario
      </Typography>
      <Grid container spacing={4}>
        <Grid >
          <Card sx={{ p: 3, textAlign: 'center' }}>
            <Avatar alt="Avatar" src={!!avatar ? avatar : undefined} sx={{ width: 150, height: 150, mb: 2, mx: 'auto' }} >
              {firstName?.charAt(0).toUpperCase()}
            </Avatar>
            <input
              type="file"
              accept="image/*"
              onChange={handleAvatarChange}
              style={{ display: 'none' }}
              id="upload-avatar"
            />
            <label htmlFor="upload-avatar">
              <Button
                variant="outlined"
                component="span"
                sx={{ mb: 2 }}
              >
                Upload new photo
              </Button>
            </label>
            <Typography variant="caption" display="block" sx={{ mb: 2 }}>
              Allowed *.jpeg, *.jpg, *.png, *.gif <br /> max size of 3 Mb
            </Typography>
            <Button
              variant="outlined"
              color="primary"
              onClick={() => setChangePasswordDialogOpen(true)}
              sx={{
                backgroundColor: '#3f51b5',
                color: '#fff',
                '&:hover': {
                  backgroundColor: '#303f9f'
                },
                borderRadius: '8px'
              }}
            >
              Cambiar Contraseña
            </Button>
          </Card>
        </Grid>
        <Grid >
          <Card>
            <CardHeader />
            <CardContent>
              <Grid container spacing={3}>
                <Grid >
                  <TextField
                    label="Email address"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    disabled={!editMode}
                    fullWidth
                  />
                </Grid>
                <Grid >
                  <TextField
                    label="First name"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                    disabled={!editMode}
                    fullWidth
                  />
                </Grid>
                <Grid>
                  <TextField
                    label="Last name"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    disabled={!editMode}
                    fullWidth
                  />
                </Grid>
              </Grid>
            </CardContent>
            <CardActions sx={{ justifyContent: 'flex-end', p: 3 }}>
              {editMode ? (
                <>
                  <Button onClick={() => setEditMode(false)} variant="contained" color='inherit'>
                    Cancel
                  </Button>
                  <Button onClick={handleSave} variant="contained" color='primary'>
                    Save changes
                  </Button>
                </>
              ) : (
                <Button onClick={() => setEditMode(true)} variant="contained">
                  Edit
                </Button>
              )}
            </CardActions>
          </Card>
        </Grid>
      </Grid>

      <Dialog
        open={changePasswordDialogOpen}
        onClose={() => setChangePasswordDialogOpen(false)}
      >
        <DialogTitle>Cambiar Contraseña</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Por favor, ingresa tu contraseña actual y la nueva contraseña.
          </DialogContentText>
          <TextField
            autoFocus
            margin="dense"
            label="Contraseña Actual"
            type={showCurrentPassword ? 'text' : 'password'}
            fullWidth
            variant="outlined"
            value={currentPassword}
            onChange={(e) => setCurrentPassword(e.target.value)}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <Button onClick={() => setShowCurrentPassword(!showCurrentPassword)}>
                    {showCurrentPassword ? 'Ocultar' : 'Mostrar'}
                  </Button>
                </InputAdornment>
              )
            }}
          />
          <TextField
            margin="dense"
            label="Nueva Contraseña"
            type={showNewPassword ? 'text' : 'password'}
            fullWidth
            variant="outlined"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <Button onClick={() => setShowNewPassword(!showNewPassword)}>
                    {showNewPassword ? 'Ocultar' : 'Mostrar'}
                  </Button>
                </InputAdornment>
              )
            }}
          />
          <TextField
            margin="dense"
            label="Confirmar Nueva Contraseña"
            type={showConfirmNewPassword ? 'text' : 'password'}
            fullWidth
            variant="outlined"
            value={confirmNewPassword}
            onChange={(e) => setConfirmNewPassword(e.target.value)}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <Button onClick={() => setShowConfirmNewPassword(!showConfirmNewPassword)}>
                    {showConfirmNewPassword ? 'Ocultar' : 'Mostrar'}
                  </Button>
                </InputAdornment>
              )
            }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setChangePasswordDialogOpen(false)} color="primary">
            Cancelar
          </Button>
          <Button
            onClick={handleChangePassword}
            color="primary"
            variant="contained"
            disabled={newPassword === '' || confirmNewPassword === ''}
          >
            Cambiar Contraseña
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}
