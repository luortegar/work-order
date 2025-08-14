import axios from './axiosInstance'
import { UserResponse } from './response/userResponse';
import { UpdatePasswordRequest } from './request/ProfileRequest';

export const viewMyUser = async () =>
    axios.get<UserResponse>(`/private/v1/users/my-user`)

export const viewUser = async (id: string) =>
    axios.get(`/private/v1/users/${id}`)

export const listUser = async (size : number = 10, page : number = 0, sort : string = '', searchTerm : string = '') =>
    axios.get(`/private/v1/users?size=${size}&page=${page}&sort=${sort}&searchTerm=${searchTerm}`)

export const createUser = async (payload: any) => 
    axios.post(`/private/v1/users`, payload)

export const updateUser = async (id: string, payload: any) => 
    axios.put(`/private/v1/users/${id}`, payload)

export const userUpdateParcial = async (id: string, payload: any) =>
    axios.patch(`/private/v1/users/${id}`, payload)

export const updateMyUserPassword = async (payload: UpdatePasswordRequest) =>
    axios.patch(`/private/v1/users/update-my-user-password`, payload)

export const deleteUser = async (id: string) =>
    axios.delete(`/private/v1/users/${id}`)

export const listClienUser = async (clientId:string, size : number = 10, page : number = 0, sort : string = '', searchTerm : string = '') =>
    axios.get(`/private/v1/employees/clients/${clientId}?size=${size}&page=${page}&sort=${sort}&searchTerm=${searchTerm}`)

export const listBranchUser = async (clientId:string, size : number = 10, page : number = 0, sort : string = '', searchTerm : string = '') =>
    axios.get(`/private/v1/employees/branch/${clientId}?size=${size}&page=${page}&sort=${sort}&searchTerm=${searchTerm}`)

export const createUserClient = async (payload: any) => 
    axios.post(`/private/v1/employees`, payload)