import axios from './axiosInstance'

export type TypeOfPurchaseResponse = {
  name: string;
  code: string;
}

export const autocompleteTypeOfPurchase = async (searchTerm : string = '') => {
    return await axios.get(`/private/v1/type-of-purchase/autocomplete?searchTerm=${searchTerm}`);
}