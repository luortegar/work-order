import axios from './axiosInstance'
import { RoleAutocompleteResponse } from './types/roleTypes'

export const viewRole = async (id: string) => await axios.get(`/private/v1/roles/${id}`)

export const listRole = async (size : number = 10, page : number = 0, sort : string = '', searchTerm : string = '') =>
    await axios.get(`/private/v1/roles?size=${size}&page=${page}&sort=${sort}&searchTerm=${searchTerm}`)

export const createRole = async (payload: any) => 
    axios.post(`/private/v1/roles`, payload)

export const updateRole = async (id: string, payload: any) => 
    await axios.put(`/private/v1/roles/${id}`, payload)

export const deleteRole = async (id: string) =>
    await axios.delete(`/private/v1/roles/${id}`)

export const autocompleteRole = async (searchTerm : string = ''): Promise<RoleAutocompleteResponse[]> =>{
    const {data} = await axios.get<RoleAutocompleteResponse[]>(`/private/v1/roles/autocomplete?searchTerm=${searchTerm}`)
    return data
}
