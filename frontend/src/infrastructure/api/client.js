import axios from 'axios'
import { API_CONFIG } from '../../core/config/environment'

const client = axios.create({
  baseURL: API_CONFIG.GATEWAY,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
})

/**
 * Agrega el token JWT
 */
client.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

/**
 * Interceptor: Maneja errores globales
 */
client.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default client