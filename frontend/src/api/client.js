import axios from 'axios'

const client = axios.create({
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
})

client.interceptors.request.use(
  (config) => {
    console.log('Petición a:', config.url)
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

client.interceptors.response.use(
  (response) => {
    console.log('Respuesta de:', response.config.url, response.status)  // 🔍 AGREGAR LOG
    return response
  },
  (error) => {
    console.error('Error en petición:', error.config?.url, error.message)  // 🔍 AGREGAR LOG
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default client