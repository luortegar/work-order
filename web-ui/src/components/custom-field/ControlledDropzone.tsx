import React, { useCallback, useState, useEffect } from "react";
import { useDropzone } from "react-dropzone";
import { useController, Control } from "react-hook-form";
import { Box, Typography, styled, Stack, useTheme } from "@mui/material";

interface ControlledDropzoneProps {
  name: string;
  control: Control<any>;
  label: string;
}

const DropzoneContainer = styled(Box)(({ theme }) => ({
  border: `2px dashed ${theme.palette.divider}`,
  borderRadius: theme.shape.borderRadius,
  padding: theme.spacing(4),
  textAlign: "center",
  cursor: "pointer",
  transition: "border-color 0.2s ease-in-out",
  "&:hover": {
    borderColor: theme.palette.primary.main,
  },
}));

const ControlledDropzone: React.FC<ControlledDropzoneProps> = ({
  name,
  control,
  label,
}) => {
  const { field } = useController({ name, control });

  const theme = useTheme();
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  // Mantiene sincronizado el estado local con el valor de React Hook Form
  useEffect(() => {
    if (field.value instanceof File) {
      setSelectedFile(field.value);
    } else if (!field.value) {
      setSelectedFile(null);
    }
  }, [field.value]);

  // Callback de dropzone
  const onDrop = useCallback(
    (acceptedFiles: File[]) => {
      if (acceptedFiles.length > 0) {
        const file = acceptedFiles[0];
        setSelectedFile(file);
        field.onChange(file);
      }
    },
    [field]
  );

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    multiple: false,
    accept: { "image/*": [".jpeg", ".png", ".jpg", ".gif"] },
  });

  return (
    <Stack spacing={2} alignItems="center">
      <DropzoneContainer {...getRootProps()}>
        <input {...getInputProps()} />
        {selectedFile ? (
          <Typography>
            Archivo seleccionado: <b>{selectedFile.name}</b>
          </Typography>
        ) : isDragActive ? (
          <Typography color="primary">Suelta el archivo aquí...</Typography>
        ) : (
          <Typography>{label}</Typography>
        )}
      </DropzoneContainer>

      {/* Preview opcional */}
      {selectedFile && (
        <Box
          mt={1}
          sx={{
            border: `1px solid ${theme.palette.divider}`,
            borderRadius: 1,
            p: 1,
            maxWidth: 200,
          }}
        >
          <img
            src={URL.createObjectURL(selectedFile)}
            alt="preview"
            style={{ maxWidth: "100%", borderRadius: 4 }}
          />
        </Box>
      )}
    </Stack>
  );
};

export default ControlledDropzone;
