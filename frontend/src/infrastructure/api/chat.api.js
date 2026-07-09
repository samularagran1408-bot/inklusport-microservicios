// src/infrastructure/api/chat.api.js
import client from './client'

const API_URL = import.meta.env.VITE_API_AI || 'http://localhost:3008'

// ✅ EXPORTAR NOMBRADO como chatApi
export const chatApi = {
  sendMessage: (message, sessionId) => 
    client.post(`${API_URL}/api/ai/chat/message`, { message, sessionId }),
  
  getSessions: () => 
    client.get(`${API_URL}/api/ai/chat/sessions`),
  
  getSession: (sessionId) => 
    client.get(`${API_URL}/api/ai/chat/sessions/${sessionId}`),
  
  closeSession: (sessionId) => 
    client.put(`${API_URL}/api/ai/chat/sessions/${sessionId}/close`),
}