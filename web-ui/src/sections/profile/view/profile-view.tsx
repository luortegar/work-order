import React, { useState, useEffect, useContext } from 'react';
import { Container, Typography, Grid } from '@mui/material';
import { viewMyUser, updateUser, userUpdateParcial, updateMyUserPassword } from 'src/api/userCrudApi';
import { uploadFile } from "src/api/fileApi";
import { UserContext } from "../../../context/user/UserContext";
import AvatarCard from './AvatarCard';
import PersonalInfoCard from './PersonalInfoCard';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';
import ChangePasswordDialog from './ChangePasswordDialog';

export default function ProfileView() {
  const [userId, setUserId] = useState('');
  const [email, setEmail] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [avatar, setAvatar] = useState('/assets/images/avatars/avatar_25.jpg');
  const [editMode, setEditMode] = useState(false);

  const { resetUser } = useContext(UserContext)!;
  const { showMessage } = useSnackbar();

  const [changePasswordDialogOpen, setChangePasswordDialogOpen] = useState(false);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const user = await viewMyUser();
        setUserId(user.data.userId);
        setEmail(user.data.email);
        setFirstName(user.data.firstName);
        setLastName(user.data.lastName);
        setAvatar(user.data.profilePictureUrl || '/assets/images/avatars/avatar_25.jpg');
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
      uploadFile(file)
        .then((response) => {
          userUpdateParcial(userId, { profilePictureFileId: response.data.fileId })
            .then((resp) => {
              setAvatar(resp.data.profilePictureUrl);
              resetUser();
            });
        })
        .catch((error) => console.error("Error uploading avatar:", error));
    }
  };

  const handleSave = () => {
    const payload = { email, firstName, lastName, avatarUrl: avatar };
    updateUser(userId, payload)
      .then(() => {
        setEditMode(false);
        resetUser();
      })
      .catch((error) => console.error("Error updating user data:", error));
  };

  const handleChangePassword = (currentPassword: string, newPassword: string) => {
    const payload = { currentPassword, newPassword };
    updateMyUserPassword(payload)
      .then(() => {
        showMessage("Password updated successfully.", "success");
        setChangePasswordDialogOpen(false);
      })
      .catch((error) => {
        const errorMessage = error?.response?.data?.message || "Unknown error!";
        showMessage(errorMessage, "error");
      });
  };

  return (
    <Container maxWidth="lg" sx={{ py: 5 }}>
      <Typography variant="h4" sx={{ mb: 4, fontWeight: 'bold' }}>User Profile</Typography>
      <Grid container spacing={4}>
        <Grid>
          <AvatarCard
            avatar={avatar}
            firstName={firstName}
            lastName={lastName}
            email={email}
            onAvatarChange={handleAvatarChange}
            onChangePasswordClick={() => setChangePasswordDialogOpen(true)}
          />
        </Grid>
        <Grid>
          <PersonalInfoCard
            email={email}
            firstName={firstName}
            lastName={lastName}
            editMode={editMode}
            setFirstName={setFirstName}
            setLastName={setLastName}
            onSave={handleSave}
            onEditToggle={setEditMode}
          />
        </Grid>
      </Grid>

      <ChangePasswordDialog
        open={changePasswordDialogOpen}
        onClose={() => setChangePasswordDialogOpen(false)}
        onPasswordChange={handleChangePassword}
      />
    </Container>
  );
}
