import axios from './axiosInstance'
import { ClientPageResponse, ClientResponse } from './types/clientTypes'

export const view = async (id: string): Promise<ClientResponse> => {
  const { data } = await axios.get<ClientResponse>(`/private/v1/clients/${id}`)
  return data
}

export const list = async (size : number = 10, page : number = 0, sort : string = '', searchTerm : string = ''):Promise<ClientPageResponse> => {
    const {data} = await axios.get<ClientPageResponse>(`/private/v1/clients?size=${size}&page=${page}&sort=${sort}&searchTerm=${searchTerm}`)
    return data
}

export const create = async (payload: any) =>
    await axios.post(`/private/v1/clients`, payload)

export const update = async (id: string, payload: any) =>
    await axios.put(`/private/v1/clients/${id}`, payload)

export const deleteById = async (id: string) =>
    await axios.delete(`/private/v1/clients/${id}`)