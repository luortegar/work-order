import axios from './axiosInstance'
import { UserAutocompleteResponse } from './types/employeesTypes';

export const autocompleteTechnicians = async (searchTerm:string): Promise<UserAutocompleteResponse[]> =>{
   const {data} = await axios.get<UserAutocompleteResponse[]>(`/private/v1/technicians/autocomplete?searchTerm=${searchTerm}`)
    return data;
}