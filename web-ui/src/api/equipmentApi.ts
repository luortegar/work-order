import axios from './axiosInstance'

export const viewEquipment = async (id: string) =>
    axios.get(`/private/v1/equipments/${id}`)

export const listEquipment = async (branchId : string, size : number = 10, page : number = 0, sort : string = '', searchTerm : string = '') =>
    axios.get(`/private/v1/equipments/branch-offices/${branchId}?size=${size}&page=${page}&sort=${sort}&searchTerm=${searchTerm}`);

export const createEquipment = async (payload: any) => 
    axios.post(`/private/v1/equipments`, payload)

export const updateEquipment = async (id: string, payload: any) =>
    axios.put(`/private/v1/equipments/${id}`, payload)

export const deleteEquipmentById = async (id: string) => 
    axios.delete(`/private/v1/equipments/${id}`)