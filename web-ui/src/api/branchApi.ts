import axios from './axiosInstance'

export const viewBranch = async (id: string) =>
    axios.get(`/private/v1/branch-offices/${id}`)

export const listBranch = async (clientId : string, size : number = 10, page : number = 0, sort : string = '', searchTerm : string = '') =>
    axios.get(`/private/v1/branch-offices/clients/${clientId}?size=${size}&page=${page}&sort=${sort}&searchTerm=${searchTerm}`)
    
export const createBranch = async (payload: any) =>
    axios.post(`/private/v1/branch-offices`, payload)
    
export const updateBranch = async (id: string, payload: any) =>
    axios.put(`/private/v1/branch-offices/${id}`, payload)

export const deleteBranchById = async (id: string) =>
    axios.delete(`/private/v1/branch-offices/${id}`)
