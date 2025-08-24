import { Pageable, Sort } from "./commonTypes"

export interface ClientResponse {
  clientId: string
  companyName: string
  uniqueTaxpayerIdentification: string
  business: string
  address: string
  commune: string
  city: string
  typeOfPurchase: string
}

export interface ClientRequest {
  clientId: string
  companyName: string
  uniqueTaxpayerIdentification: string
  business: string
  address: string
  commune: string
  city: string
  typeOfPurchase: string
}


export interface ClientPageResponse {
  content: ClientResponse[]
  pageable: Pageable
  last: boolean
  totalElements: number
  totalPages: number
  first: boolean
  size: number
  number: number
  sort: Sort
  numberOfElements: number
  empty: boolean
}
