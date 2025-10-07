import axios from './axiosInstance'
import { FileResponse } from './types/commonTypes';

export const view = async (id: string) =>
    axios.get(`/private/v1/inspection-visits/${id}`)

export const list = async (branchId : string, size : number = 10, page : number = 0, sort : string = '', searchTerm : string = '') =>
    axios.get(`/private/v1/inspection-visits/branch-offices/${branchId}?size=${size}&page=${page}&sort=${sort}&searchTerm=${searchTerm}`);

export const create = async (payload: any) => 
    axios.post(`/private/v1/inspection-visits`, payload)

export const update = async (id: string, payload: any) =>
    axios.put(`/private/v1/inspection-visits/${id}`, payload)

export const deleteById = async (id: string) => 
    axios.delete(`/private/v1/inspection-visits/${id}`)


export const viewPhotos = async (id: string): Promise<FileResponse[]> => {
    const { data } = await axios.get<FileResponse[]>(`/private/v1/inspection-visits/${id}/visit-photo`)
    return data;
}

export const uploadPhoto = async (
  file: File,
  id: string,
  onProgress: (progress: number) => void
) => {
  const formData = new FormData();
  formData.append("file", file);

  await axios.patch(`/private/v1/inspection-visits/${id}/visit-photo`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
    // Permitir hasta 5 minutos de subida
    timeout: 300000, // 5 min en milisegundos
    maxContentLength: Infinity, // Permite cuerpos grandes
    maxBodyLength: Infinity,
    onUploadProgress: (progressEvent) => {
      if (progressEvent.total) {
        const percentCompleted =
          (progressEvent.loaded / progressEvent.total) * 100;
        onProgress(percentCompleted);
      }
    },
  });
};

export const deletePhoto = async (id: string, workOrderPhotoId:string) =>
    axios.delete(`/private/v1/inspection-visits/${id}/visit-photo/${workOrderPhotoId}`)