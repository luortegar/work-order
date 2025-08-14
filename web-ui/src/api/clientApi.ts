import axios from './axiosInstance'

export const view = async (id: string) =>
    await axios.get(`/private/v1/clients/${id}`)

export const list = async (size : number = 10, page : number = 0, sort : string = '', searchTerm : string = '') => 
    await axios.get(`/private/v1/clients?size=${size}&page=${page}&sort=${sort}&searchTerm=${searchTerm}`)

export const create = async (payload: any) =>
    await axios.post(`/private/v1/clients`, payload)

export const update = async (id: string, payload: any) =>
    await axios.put(`/private/v1/clients/${id}`, payload)

export const deleteById = async (id: string) =>
    await axios.delete(`/private/v1/clients/${id}`)