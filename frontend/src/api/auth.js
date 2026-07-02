import client from './client'

const API_URL = import.meta.env.VITE_API_AUTH

export const authApi = {
  login: (credentials) => 
    client.post(`${API_URL}/api/auth/login`, credentials),
  
  register: (userData) => 
    client.post(`${API_URL}/api/auth/register`, userData),
  
  logout: () => 
    client.post(`${API_URL}/api/auth/logout`),
  
  validateToken: () => 
    client.get(`${API_URL}/api/auth/validate`),
  
  forgotPassword: (email) => 
    client.post(`${API_URL}/api/auth/forgot-password`, { email }),
  
  resetPassword: (data) => 
    client.post(`${API_URL}/api/auth/reset-password`, data),
}