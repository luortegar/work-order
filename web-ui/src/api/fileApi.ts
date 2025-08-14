import axios from './axiosInstance'
import FormData from 'form-data';

export const viewFile = async (id: string) =>
  axios.get(`/private/v1/files/${id}`)

export const downloadFile = async (id: string) =>
  axios.get(`/public/v1/files/${id}/download`)

export const uploadFile = async (file: File) => {
    const data = new FormData();
    data.append('file', file);
    return await axios.post(`/private/v1/files`, data, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
}

export const deleteFile = async (id: string) =>
  axios.delete(`/private/v1/work-orders/${id}`)