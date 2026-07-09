import React, { createContext, useState, useContext, useEffect } from 'react'
import { authApi } from '../../infrastructure/api/auth.api'

const AuthContext = createContext()

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)
  const [token, setToken] = useState(localStorage.getItem('token'))

  const login = async (credentials) => {
    try {
      const response = await authApi.login(credentials)
      const { token, user } = response.data
      localStorage.setItem('token', token)
      setToken(token)
      setUser(user)
      return { success: true, data: response.data }
    } catch (error) {
      return {
        success: false,
        error: error.response?.data?.message || 'Error al iniciar sesión'
      }
    }
  }

  const register = async (userData) => {
    try {
      const response = await authApi.register(userData)
      const { token, user } = response.data
      localStorage.setItem('token', token)
      setToken(token)
      setUser(user)
      return { success: true, data: response.data }
    } catch (error) {
      return {
        success: false,
        error: error.response?.data?.message || 'Error al registrarse'
      }
    }
  }

  const logout = () => {
    localStorage.removeItem('token')
    setToken(null)
    setUser(null)
  }

  const validateToken = async () => {
    try {
      const response = await authApi.validateToken()
      setUser(response.data)
      return true
    } catch {
      logout()
      return false
    }
  }

  useEffect(() => {
    const loadUser = async () => {
      if (token) {
        await validateToken()
      }
      setLoading(false)
    }
    loadUser()
  }, [token])

  const value = {
    user,
    token,
    loading,
    login,
    register,
    logout,
    isAuthenticated: !!token && !!user,
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth debe usarse dentro de AuthProvider')
  }
  return context
}