import axios from './axiosInstance'

export type CommuneResponse = {
  country: string;
  region: string;
  commune: string;
}

export const autocompleteCommune = async (searchTerm : string = '') =>
    axios.get(`/private/v1/addresses/commune/autocomplete?searchTerm=${searchTerm}`)