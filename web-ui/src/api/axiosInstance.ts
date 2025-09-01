import axios, { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig, AxiosError } from 'axios'
import { jwtDecode } from 'jwt-decode'

const baseURL = import.meta.env.VITE_BASE_SERVER_URL;

interface MyToken {
  exp: number; // unix timestamp
  jti: string;
}

// 🔹 Función para verificar si el token expiró
const isTokenExpired = (token: string): boolean => {
  try {
    const decoded = jwtDecode<MyToken>(token);

    console.log('decoded.exp', decoded.exp)

    if (!decoded.exp) return true;
    const now = Math.floor(Date.now() / 1000);
    return decoded.exp < now;
  } catch (e) {
    console.error("Error decoding token", e);
    return true;
  }
};

// 🔹 Instancia de Axios
const axiosInstance: AxiosInstance = axios.create({
  baseURL,
  timeout: 5000,
});

// 🔹 Variable para evitar múltiples refresh simultáneos
let isRefreshing = false;
let failedQueue: any[] = [];

const processQueue = (error: any, token: string | null = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });

  failedQueue = [];
};

// 🔹 Interceptor de request
axiosInstance.interceptors.request.use(
  async (config: InternalAxiosRequestConfig) => {
    let token = sessionStorage.getItem('token');
    const refreshToken = sessionStorage.getItem('refreshToken');

    console.log("token---", token)
    const isTokenExpiredNow = token ? isTokenExpired(token) : true;
    console.log("isTokenExpiredNow", isTokenExpiredNow)

    if (token && isTokenExpiredNow && refreshToken) {
      if (!isRefreshing) {
        isRefreshing = true;

        try {
          // 👇 Aquí llamas al endpoint de refresh
          const response = await axios.post(`${baseURL}public/v1/auth/refresh-token`, {
            refreshToken,
          });

          token = response.data.token;
          if(token){
            sessionStorage.setItem('token', token);
          }
          isRefreshing = false;
          processQueue(null, token);
        } catch (err) {
          isRefreshing = false;
          processQueue(err, null);

          console.log('---sign-in 1----')
          sessionStorage.clear();
          window.location.href = '/sign-in';

          return Promise.reject(err);
        }
      }

      // Espera a que el refresh termine antes de continuar
      return new Promise((resolve, reject) => {
        failedQueue.push({
          resolve: (newToken: string) => {
            if (config.headers) {
              config.headers['Authorization'] = `Bearer ${newToken}`;
            }
            resolve(config);
          },
          reject: (err: any) => reject(err),
        });
      });
    }

    // Si el token es válido, lo agregamos
    if (token && config.headers) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error: AxiosError) => Promise.reject(error)
);

// 🔹 Interceptor de response
axiosInstance.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error: AxiosError) => {
    console.error('Error en la solicitud:', error);

    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      sessionStorage.clear();
      window.location.href = '/sign-in';
      console.log("------------error-login---11-----------")
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;
