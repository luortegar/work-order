import axios from './axiosInstance'
import { UserAutocompleteResponse } from './types/employeesTypes';

export const autocompleteEmployeesAndFilterByBranchId = async (branchId:string, searchTerm:string): Promise<UserAutocompleteResponse[]> =>{
   const {data} = await axios.get<UserAutocompleteResponse[]>(`/private/v1/employees/branch/${branchId}/autocomplete?searchTerm=${searchTerm}`)
    return data;
}