export interface Sort {
  empty: boolean
  unsorted: boolean
  sorted: boolean
}

export interface Pageable {
  pageNumber: number
  pageSize: number
  sort: Sort
  offset: number
  paged: boolean
  unpaged: boolean
}

export interface FileResponse {
  fileId: string; 
  fileName: string;
  link: string;
  referenceId:string;
}
