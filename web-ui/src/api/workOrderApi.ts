import axios from './axiosInstance'
import { FileResponse } from './types/commonTypes';
import { DetailedWorkOrderResponse } from './types/workOrderTypes';

export const viewWorkOrder = async (id: string): Promise<DetailedWorkOrderResponse> => {
    const { data } = await axios.get<DetailedWorkOrderResponse>(`/private/v1/work-orders/${id}`)
    return data;
}



export const list = async (size: number = 10, page: number = 0, sort: string = '', searchTerm: string = '', workOrderStatus: string | null = null) =>
    axios.get(`/private/v1/work-orders?size=${size}&page=${page}&sort=${sort}&searchTerm=${searchTerm}&workOrderStatus=${workOrderStatus}`)

export const create = async (payload: any) =>
    axios.post(`/private/v1/work-orders`, payload)
export const update = async (id: string, payload: any) =>
    axios.put(`/private/v1/work-orders/${id}`, payload)

export const deleteById = async (id: string) =>
    axios.delete(`/private/v1/work-orders/${id}`)

export const viewPDF1 = async (id: string) =>
    axios.get(`/private/v1/work-orders/${id}/pdf`)

export const viewPDF = async (id: string) => {
    try {
        // Hacer la solicitud con responseType 'blob' para manejar archivos
        const response = await axios.get(`/private/v1/work-orders/${id}/pdf`, {
            responseType: 'blob', // Configurar la respuesta como 'blob' para archivos binarios
        });

        // Crear una URL para el blob
        const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));

        // Crear un enlace temporal para la descarga
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', `work-order-${id}.pdf`); // Nombre del archivo de descarga
        document.body.appendChild(link);
        link.click();

        // Limpiar el enlace temporal
        link.parentNode?.removeChild(link);
        window.URL.revokeObjectURL(url);
    } catch (error) {
        console.error("Error al descargar el archivo PDF:", error);
    }
};

export const uploadPhotoOld = async (file: File, id: string) => {
    const data = new FormData();
    data.append('file', file);
    return await axios.patch(`/private/v1/work-orders/${id}/work-order-photo`, data, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    })
}

export const viewPhotosOfAWorkOrder = async (id: string): Promise<FileResponse[]> => {
    const { data } = await axios.get<FileResponse[]>(`/private/v1/work-orders/${id}/work-order-photo`)
    return data;
}

export const uploadPhoto = async (
  file: File,
  id: string,
  onProgress: (progress: number) => void
) => {
  const formData = new FormData();
  formData.append("file", file);

  await axios.patch(`/private/v1/work-orders/${id}/work-order-photo`, formData, {
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

export const deletePhotoOfAWorkOrder = async (id: string, workOrderPhotoId:string) =>
    axios.delete(`/private/v1/work-orders/${id}/work-order-photo/${workOrderPhotoId}`)