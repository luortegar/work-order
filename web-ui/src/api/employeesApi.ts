import axios from './axiosInstance'
import { EmployeesAutocompleteResponse } from './types/employeesTypes';

export const autocompleteEmployeesAndFilterByBranchId = async (branchId:string, searchTerm:string): Promise<EmployeesAutocompleteResponse[]> =>{
   const {data} = await axios.get<EmployeesAutocompleteResponse[]>(`/private/v1/employees/branch/${branchId}/autocomplete?searchTerm=${searchTerm}`)
    return data;
}