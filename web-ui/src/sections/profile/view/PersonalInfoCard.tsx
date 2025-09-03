import React from 'react';
import { Card, CardHeader, CardContent, Grid, TextField, CardActions, Button } from '@mui/material';
import { Edit as EditIcon, Save as SaveIcon } from '@mui/icons-material';

interface PersonalInfoCardProps {
  email: string;
  firstName: string;
  lastName: string;
  editMode: boolean;
  setFirstName: (value: string) => void;
  setLastName: (value: string) => void;
  onSave: () => void;
  onEditToggle: (value: boolean) => void;
}

export default function PersonalInfoCard({
  email,
  firstName,
  lastName,
  editMode,
  setFirstName,
  setLastName,
  onSave,
  onEditToggle
}: PersonalInfoCardProps) {
  return (
    <Card sx={{ borderRadius: 4, boxShadow: 3 }}>
      <CardHeader title="Personal Information" sx={{ pb: 0 }} />
      <CardContent>
        <Grid container spacing={3}>
          <Grid>
            <TextField label="Email" value={email} disabled fullWidth />
          </Grid>
          <Grid>
            <TextField
              label="First Name"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
              disabled={!editMode}
              fullWidth
            />
          </Grid>
          <Grid>
            <TextField
              label="Last Name"
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
            <Button onClick={() => onEditToggle(false)} color="primary">Cancel</Button>
            <Button onClick={onSave} variant="contained" startIcon={<SaveIcon />} color="primary">
              Save
            </Button>
          </>
        ) : (
          <Button onClick={() => onEditToggle(true)} variant="contained" startIcon={<EditIcon />}>
            Edit
          </Button>
        )}
      </CardActions>
    </Card>
  );
}
