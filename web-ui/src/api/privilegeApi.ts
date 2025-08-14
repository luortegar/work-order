import axios from './axiosInstance'

export const findAllPrivileges = async () => {
    return await axios.get('/private/v1/privileges');
}

