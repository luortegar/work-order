import axios from './axiosInstance'

export const tenantList = async (size : number = 100, page : number = 0, sort : string = '', searchTerm : string = '') =>
    await axios.get(`/public/v1/tenants?size=${size}&page=${page}&sort=${sort}&searchTerm=${searchTerm}`, {headers: {Authorization: null}})