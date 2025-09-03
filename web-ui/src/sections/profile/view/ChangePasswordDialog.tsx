import React, { useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContentText,
  TextField,
  DialogContent,
  InputAdornment,
  IconButton,
  DialogActions,
  Button
} from '@mui/material';
import {Iconify} from 'src/components/iconify';
import { SaveIcon } from 'lucide-react';

interface ChangePasswordDialogProps {
  open: boolean;
  onClose: () => void;
  onPasswordChange: (currentPassword: string, newPassword: string) => void;
}

interface Errors {
  currentPassword: boolean;
  newPassword: boolean;
  confirmNewPassword: boolean;
}

export default function ChangePasswordDialog({ open, onClose, onPasswordChange }: ChangePasswordDialogProps) {
  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmNewPassword, setConfirmNewPassword] = useState('');
  const [showCurrentPassword, setShowCurrentPassword] = useState(false);
  const [showNewPassword, setShowNewPassword] = useState(false);
  const [showConfirmNewPassword, setShowConfirmNewPassword] = useState(false);
  const [errors, setErrors] = useState<Errors>({
    currentPassword: false,
    newPassword: false,
    confirmNewPassword: false
  });

  const validateForm = () => {
    const newErrors: Errors = {
      currentPassword: currentPassword === '',
      newPassword: newPassword === '' || newPassword.length < 6,
      confirmNewPassword: confirmNewPassword === '' || confirmNewPassword !== newPassword
    };

    setErrors(newErrors);
    return !Object.values(newErrors).some(error => error);
  };

  const handleSave = () => {
    if (validateForm()) {
      onPasswordChange(currentPassword, newPassword);
      setCurrentPassword('');
      setNewPassword('');
      setConfirmNewPassword('');
      setErrors({
        currentPassword: false,
        newPassword: false,
        confirmNewPassword: false
      });
    }
  };

  const handleClose = () => {
    setCurrentPassword('');
    setNewPassword('');
    setConfirmNewPassword('');
    setErrors({
      currentPassword: false,
      newPassword: false,
      confirmNewPassword: false
    });
    onClose();
  };



  return (
    <Dialog open={open} onClose={handleClose}>
      <DialogTitle>Change Password</DialogTitle>
      <DialogContent>
        <DialogContentText>
          Please enter your current password and the new password.
        </DialogContentText>
        <TextField
          autoFocus
          margin="dense"
          label="Current Password"
          type={showCurrentPassword ? 'text' : 'password'}
          fullWidth
          variant="outlined"
          value={currentPassword}
          error={errors.currentPassword}
          helperText={errors.currentPassword && 'Current password is required'}
          onChange={(e) => setCurrentPassword(e.target.value)}
          InputProps={{
            endAdornment: (
              <InputAdornment position="end">
                <IconButton onClick={() => setShowCurrentPassword((prev) => !prev)} edge="end">
                  <Iconify icon={showCurrentPassword ? 'solar:eye-bold' : 'solar:eye-closed-bold'} />
                </IconButton>
              </InputAdornment>
            ),
          }}
        />
        <TextField
          margin="dense"
          label="New Password"
          type={showNewPassword ? 'text' : 'password'}
          fullWidth
          variant="outlined"
          value={newPassword}
          error={errors.newPassword}
          helperText={errors.newPassword && (newPassword === '' ? 'New password is required' : 'Password must be at least 6 characters')}
          onChange={(e) => setNewPassword(e.target.value)}
          InputProps={{
            endAdornment: (
              <InputAdornment position="end">
                <IconButton onClick={() => setShowNewPassword((prev) => !prev)} edge="end">
                  <Iconify icon={showNewPassword ? 'solar:eye-bold' : 'solar:eye-closed-bold'} />
                </IconButton>
              </InputAdornment>
            ),
          }}
        />
        <TextField
          margin="dense"
          label="Confirm New Password"
          type={showConfirmNewPassword ? 'text' : 'password'}
          fullWidth
          variant="outlined"
          value={confirmNewPassword}
          error={errors.confirmNewPassword}
          helperText={errors.confirmNewPassword && (confirmNewPassword === '' ? 'Please confirm your new password' : 'Passwords do not match')}
          onChange={(e) => setConfirmNewPassword(e.target.value)}
          InputProps={{
            endAdornment: (
              <InputAdornment position="end">
                <IconButton onClick={() => setShowConfirmNewPassword((prev) => !prev)} edge="end">
                  <Iconify icon={showConfirmNewPassword ? 'solar:eye-bold' : 'solar:eye-closed-bold'} />
                </IconButton>
              </InputAdornment>
            ),
          }}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose} color="primary">Cancel</Button>
        <Button
          onClick={handleSave}
          color="primary"
          variant="contained"
          startIcon={<SaveIcon />}
          disabled={!currentPassword || !newPassword || !confirmNewPassword}
        >
          Save
        </Button>
      </DialogActions>
    </Dialog>
  );
}