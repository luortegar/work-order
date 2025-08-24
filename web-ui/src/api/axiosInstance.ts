import axios, { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig, AxiosError } from 'axios'

const baseURL =  import.meta.env.VITE_BASE_SERVER_URL;


const axiosInstance: AxiosInstance = axios.create({
  baseURL,
  timeout: 5000,
})

axiosInstance.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error: AxiosError) => {
    console.error('Error en la solicitud:', error);
    
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      //sessionStorage.clear()
      //window.location.href = '/sign-in'
    }
    
    return Promise.reject(error)
  }
)

axiosInstance.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = sessionStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error: AxiosError) => {
    return Promise.reject(error)
  }
)

export default axiosInstance;
