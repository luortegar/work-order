import axios from './axiosInstance'
import {ChangePasswordAndLoginWithCode, AuthenticateRequest, AuthenticationWithCodeRequest} from './request/AuthenticateRequest'

export const sigIn = async (authenticateRequest: AuthenticateRequest) =>
    axios.post('/public/v1/auth/authenticate', authenticateRequest, {headers: {Authorization: null}})

export const findAll = async () =>
    axios.get('/private/v1/users')

export const sigUp = (data:any) => 
    axios.post('/public/v1/auth/register', data, {headers: {}})

export const recoveryPassword = (data:any) =>
    axios.post('/public/v1/auth/recovery-password', data, {headers: {}})

export const authenticateWithCode = async (authenticationWithCodeRequest: AuthenticationWithCodeRequest) =>
    axios.post('/public/v1/auth/authenticate-with-code', authenticationWithCodeRequest, {headers: {Authorization: null}})

export const changePasswordAndLoginWithCode = async (changePasswordAndLoginWithCode: ChangePasswordAndLoginWithCode) =>
    axios.post('/public/v1/auth/change-password-and-login-with-code', changePasswordAndLoginWithCode, {headers: {Authorization: null}})
