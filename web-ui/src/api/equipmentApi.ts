import axios from './axiosInstance'
import { EquipmentAutocompleteResponse, EquipmentTypeResponse } from './types/equitmentTypes';

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

export const autocompleteEquipmentType = async (searchTerm : string = ''): Promise<EquipmentTypeResponse[]> =>{
    const {data} = await axios.get<EquipmentTypeResponse[]>(`/private/v1/equipments/equipment-type/autocomplete?searchTerm=${searchTerm}`)
    return data;
}
export const autocompleteEquipmentAndFilterByBranchId = async (branchId : string, searchTerm : string = ''): Promise<EquipmentAutocompleteResponse[]> =>{
    const {data} =  await axios.get<EquipmentAutocompleteResponse[]>(`/private/v1/equipments/branch-offices/${branchId}/autoComplete?searchTerm=${searchTerm}`);
    return data;
}
