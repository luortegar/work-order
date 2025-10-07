import React, { useEffect, useState } from "react";
import {
  Box,
  Card,
  CardMedia,
  IconButton,
  Dialog,
  DialogContent,
  DialogTitle,
  Button,
  Typography,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import ZoomInIcon from "@mui/icons-material/ZoomIn";
import { FileResponse } from "src/api/types/commonTypes";



interface Props {
  id:string;
  photoList: FileResponse[];
  deletePhoto: (id:string, idPhoto:string)=> Promise<any>;
  refreshPhotos: () => void
}

const PhotoGallery: React.FC<Props> = ({ photoList, id, deletePhoto, refreshPhotos }) => {
  const [photos, setPhotos] = useState<FileResponse[]>(photoList);
  const [selected, setSelected] = useState<FileResponse | null>(null);

  const handleDelete = (photoId: string) => {
      deletePhoto(id, photoId ).then((rsp)=>{
        console.log(rsp)
        refreshPhotos()
      })
  };

  const handleClearAll = () => {
    setPhotos([]);
    setSelected(null);
  };

  useEffect(() => {
    setPhotos(photoList)
  }, [photoList]);

  return (
    <Box>
      <Box
        sx={{
          display: "grid",
          gridTemplateColumns: "repeat(auto-fill, minmax(120px, 1fr))",
          gap: 2,
        }}
      >
        {photos.map((photo) => (
          <Card
            key={photo.fileId}
            sx={{ position: "relative", borderRadius: 1, overflow: "hidden" }}
          >
            <CardMedia
              component="img"
              src={photo.link}
              alt={photo.fileName}
              sx={{ height: 100, objectFit: "cover", cursor: "pointer" }}
              onClick={() => setSelected(photo)}
            />
            <Box
              sx={{
                position: "absolute",
                top: 0,
                right: 0,
                display: "flex",
                gap: 1,
                p: 0.5,
                background:
                  "linear-gradient(180deg, rgba(0,0,0,0.5) 0%, transparent 80%)",
              }}
            >
              <IconButton
                size="small"
                color="inherit"
                onClick={() => setSelected(photo)}
              >
                <ZoomInIcon fontSize="small" />
              </IconButton>
              <IconButton
                size="small"
                color="inherit"
                onClick={() => handleDelete(photo.referenceId)}
              >
                <DeleteIcon fontSize="small" />
              </IconButton>
            </Box>
          </Card>
        ))}
      </Box>

      <Dialog
        open={Boolean(selected)}
        onClose={() => setSelected(null)}
        maxWidth="md"
        fullWidth
      >
        <DialogTitle>{selected?.fileName}</DialogTitle>
        <DialogContent>
          {selected ? (
            <Box
              component="img"
              src={selected.link}
              alt={selected.fileName}
              sx={{ width: "100%", borderRadius: 2 }}
            />
          ) : (
            <Typography>No image selected</Typography>
          )}
        </DialogContent>
      </Dialog>
    </Box>
  );
};

export default PhotoGallery;
