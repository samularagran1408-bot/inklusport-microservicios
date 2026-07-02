import React from 'react'
import Header from '../components/common/Header'
import { useAuth } from '../contexts/AuthContext'

const Profile = () => {
  const { user } = useAuth()

  return (
    <div className="page">
      <Header />
      <main className="profile-container">
        <h1>👤 Perfil</h1>
        <div className="profile-card">
          <p><strong>Nombre:</strong> {user?.name || 'No disponible'}</p>
          <p><strong>Email:</strong> {user?.email || 'No disponible'}</p>
          <p><strong>Tipo de Discapacidad:</strong> {user?.disabilityType || 'No especificado'}</p>
        </div>
      </main>
    </div>
  )
}

export default Profile