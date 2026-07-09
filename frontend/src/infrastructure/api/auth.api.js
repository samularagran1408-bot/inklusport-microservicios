import client from './client'
import { API_CONFIG } from '../../core/config/environment'

const BASE_URL = API_CONFIG.AUTH

export const authApi = {
  login: (credentials) => 
    client.post(`${BASE_URL}/api/auth/login`, credentials),

  register: (userData) => 
    client.post(`${BASE_URL}/api/auth/register`, userData),

  logout: () => 
    client.post(`${BASE_URL}/api/auth/logout`),

  validateToken: () => 
    client.get(`${BASE_URL}/api/auth/validate`),

  forgotPassword: (email) => 
    client.post(`${BASE_URL}/api/auth/forgot-password`, { email }),

  resetPassword: (data) => 
    client.post(`${BASE_URL}/api/auth/reset-password`, data),
}