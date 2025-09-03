import React from 'react';
import { Card, Avatar, Typography, Button, IconButton } from '@mui/material';
import { Lock as LockIcon, Upload as UploadIcon } from '@mui/icons-material';

interface AvatarCardProps {
  avatar: string;
  firstName: string;
  lastName: string;
  email: string;
  onAvatarChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onChangePasswordClick: () => void;
}

export default function AvatarCard({
  avatar,
  firstName,
  lastName,
  email,
  onAvatarChange,
  onChangePasswordClick
}: AvatarCardProps) {
  return (
    <Card sx={{ p: 4, textAlign: 'center', borderRadius: 3, boxShadow: '0px 4px 20px rgba(0,0,0,0.05)' }}>
      <IconButton
        sx={{
          p: '2px',
          width: 150,
          height: 150,
          background: (theme) =>
            `conic-gradient(${theme.vars.palette.primary.light}, ${theme.vars.palette.warning.light}, ${theme.vars.palette.primary.light})`
        }}
      >
        <Avatar src={avatar} sx={{ width: 1, height: 1 }}>
        </Avatar>
      </IconButton>
      <Typography variant="h6" fontWeight="bold">{`${firstName} ${lastName}`}</Typography>
      <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>{email}</Typography>

      <input
        type="file"
        accept="image/*"
        onChange={onAvatarChange}
        style={{ display: 'none' }}
        id="upload-avatar"
      />
      <label htmlFor="upload-avatar">
        <Button
          variant="outlined"
          startIcon={<UploadIcon />}
          component="span"
          sx={{ mt: 1 }}
        >
          Change Photo
        </Button>
      </label>
      <Typography variant="caption" display="block" sx={{ mt: 1, color: 'text.secondary' }}>
        *.jpeg, *.jpg, *.png, *.gif - max. 3 MB
      </Typography>

      <Button
        variant="contained"
        color="primary"
        startIcon={<LockIcon />}
        sx={{ mt: 3, borderRadius: 2 }}
        onClick={onChangePasswordClick}
      >
        Change Password
      </Button>
    </Card>
  );
}
