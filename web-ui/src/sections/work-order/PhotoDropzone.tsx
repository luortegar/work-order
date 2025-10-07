import React, { useCallback, useState, useEffect } from "react";
import { useDropzone } from "react-dropzone";
import {
  Box,
  Typography,
  styled,
  Stack,
  LinearProgress,
  useTheme,
} from "@mui/material";

interface SimpleDropzoneProps {
  label: string;
  workOrderId: string;
  uploadFile: (
    file: File,
    id: string,
    onProgress: (progress: number) => void
  ) => Promise<any>;
  refreshScreen: () => void;
}

const DropzoneContainer = styled(Box)(({ theme }) => ({
  width: "100%",
  border: `2px dashed ${theme.palette.divider}`,
  borderRadius: theme.shape.borderRadius,
  padding: theme.spacing(2),
  textAlign: "center",
  cursor: "pointer",
  position: "relative",
  maxHeight: 150,
  minHeight: 100,
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  overflow: "hidden",
  transition: "border-color 0.2s ease-in-out",
  "&:hover": {
    borderColor: theme.palette.primary.main,
  },
}));

const ImagePreview = styled("img")({
  maxHeight: 100,
  maxWidth: 100,
  objectFit: "contain",
  borderRadius: 4,
  display: "block",
});

const OverlayProgress = styled(Box)(({ theme }) => ({
  position: "absolute",
  bottom: 0,
  left: 0,
  width: "100%",
}));

const SimpleDropzone: React.FC<SimpleDropzoneProps> = ({
  label,
  workOrderId,
  uploadFile,
  refreshScreen,
}) => {
  const theme = useTheme();
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);
  const [progress, setProgress] = useState<number>(0);
  const [loading, setLoading] = useState(false);

  // Limpia la URL de preview al desmontar o cuando cambie el archivo
  useEffect(() => {
    return () => {
      if (previewUrl) URL.revokeObjectURL(previewUrl);
    };
  }, [previewUrl]);

  const onDrop = useCallback(
    (acceptedFiles: File[]) => {
      if (acceptedFiles.length > 0) {
        const file = acceptedFiles[0];
        setSelectedFile(file);
        const url = URL.createObjectURL(file);
        setPreviewUrl(url);
        setLoading(true);
        setProgress(0);

        uploadFile(file, workOrderId, (p) => setProgress(p))
          .then(() => {
            // Limpia el componente al finalizar
            setLoading(false);
            setSelectedFile(null);
            setProgress(0);
            if (previewUrl) URL.revokeObjectURL(previewUrl);
            setPreviewUrl(null);
            refreshScreen();
          })
          .catch(() => setLoading(false));
      }
    },
    [uploadFile, workOrderId, previewUrl, refreshScreen]
  );

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    multiple: false,
    accept: { "image/*": [".jpeg", ".png", ".jpg", ".gif"] },
  });

  return (
    <Stack spacing={2} width="100%">
      <DropzoneContainer {...getRootProps()}>
        <input {...getInputProps()} />
        {selectedFile && previewUrl ? (
          <ImagePreview src={previewUrl} alt="preview" />
        ) : isDragActive ? (
          <Typography color="primary">
            {label} (Suelta el archivo aquí)
          </Typography>
        ) : (
          <Typography>{label}</Typography>
        )}

        {loading && (
          <OverlayProgress>
            <LinearProgress variant="determinate" value={progress} />
            <Typography variant="body2" color="textSecondary" align="center">
              Subiendo: {Math.round(progress)}%
            </Typography>
          </OverlayProgress>
        )}
      </DropzoneContainer>
    </Stack>
  );
};

export default SimpleDropzone;
