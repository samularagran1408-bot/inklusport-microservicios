import client from './client'

const API_URL = import.meta.env.VITE_API_SPORTS

export const sportsApi = {
  getSports: () => 
    client.get(`${API_URL}/api/sports`),
  
  getSport: (id) => 
    client.get(`${API_URL}/api/sports/${id}`),
  
  getEvents: (params) => 
    client.get(`${API_URL}/api/events`, { params }),
  
  getEvent: (id) => 
    client.get(`${API_URL}/api/events/${id}`),
  
  createEvent: (data) => 
    client.post(`${API_URL}/api/events`, data),
  
  registerToEvent: (eventId) => 
    client.post(`${API_URL}/api/registrations`, { eventId }),
}