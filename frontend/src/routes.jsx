// src/routes.jsx
import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import Login from './ui/pages/Login'
import Register from './ui/pages/Register'
import ForgotPassword from './ui/pages/ForgotPassword'
import ResetPassword from './ui/pages/ResetPassword'
import Dashboard from './ui/pages/Dashboard'

// ✅ Componente para rutas protegidas
const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem('token')
  if (!token) {
    return <Navigate to="/login" replace />
  }
  return children
}

const AppRoutes = () => {
  return (
    <Routes>
      {/* Redirigir raíz a login */}
      <Route path="/" element={<Navigate to="/login" replace />} />
      
      {/* Rutas públicas */}
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/forgot-password" element={<ForgotPassword />} />
      <Route path="/reset-password/:token" element={<ResetPassword />} />
      
      {/* ✅ Ruta protegida: Dashboard */}
      <Route 
        path="/dashboard" 
        element={
          <ProtectedRoute>
            <Dashboard />
          </ProtectedRoute>
        } 
      />
    </Routes>
  )
}

export default AppRoutes