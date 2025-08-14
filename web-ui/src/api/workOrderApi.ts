import axios from './axiosInstance'

export const view = async (id: string) =>
    axios.get(`/private/v1/work-orders/${id}`)

export const list = async (size : number = 10, page : number = 0, sort : string = '', searchTerm : string = '') => 
    axios.get(`/private/v1/work-orders?size=${size}&page=${page}&sort=${sort}&searchTerm=${searchTerm}`)

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