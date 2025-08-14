import axios from './axiosInstance'

export const autocompleteCommune = async (searchTerm : string = '') =>
    axios.get(`/private/v1/addresses/commune/autocomplete?searchTerm=${searchTerm}`)