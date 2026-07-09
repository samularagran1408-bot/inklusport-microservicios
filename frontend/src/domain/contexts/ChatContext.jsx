import React, { createContext, useState, useContext } from 'react'
import { chatApi } from '../../infrastructure/api/chat.api'

const ChatContext = createContext()

/**
 * EXPORTAR COMO ChatProvider (NOMBRADO)
 * @param {*} param0 
 */
export const ChatProvider = ({ children }) => {
  const [sessionId, setSessionId] = useState(null)
  const [messages, setMessages] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const sendMessage = async (message) => {
    setLoading(true)
    setError(null)

    try {
      const response = await chatApi.sendMessage(message, sessionId)
      const { sessionId: newSessionId, response: botResponse } = response.data

      setSessionId(newSessionId)
      setMessages(prev => [
        ...prev,
        { text: message, sender: 'user', timestamp: new Date() },
        { text: botResponse, sender: 'bot', timestamp: new Date() }
      ])

      return { success: true, data: response.data }
    } catch (error) {
      const errorMsg = error.response?.data?.message || 'Error al enviar mensaje'
      setError(errorMsg)
      return { success: false, error: errorMsg }
    } finally {
      setLoading(false)
    }
  }

  const clearChat = () => {
    setMessages([])
    setSessionId(null)
    setError(null)
  }

  const value = {
    sessionId,
    messages,
    loading,
    error,
    sendMessage,
    clearChat,
  }

  return (
    <ChatContext.Provider value={value}>
      {children}
    </ChatContext.Provider>
  )
}

/**
 * EXPORTAR useChat como NOMBRADO
 */
export const useChat = () => {
  const context = useContext(ChatContext)
  if (!context) {
    throw new Error('useChat debe usarse dentro de ChatProvider')
  }
  return context
}