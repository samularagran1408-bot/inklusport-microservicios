import React from 'react'
import Header from '../components/common/Header'
import { useAuth } from '../contexts/AuthContext'

const Dashboard = () => {
  const { user } = useAuth()

  return (
    <div className="page">
      <Header />
      <main className="dashboard-container">
        <h1>Dashboard</h1>
        <p>Bienvenido, {user?.name || 'Usuario'}!</p>
        <div className="dashboard-cards">
          <div className="card">
            <h3>Estadísticas</h3>
            <p>Próximamente...</p>
          </div>
          <div className="card">
            <h3>Deportes</h3>
            <p>Próximamente...</p>
          </div>
          <div className="card">
            <h3>Eventos</h3>
            <p>Próximamente...</p>
          </div>
          <div className="card">
            <h3>Chatbot</h3>
            <p>Próximamente...</p>
          </div>
        </div>
      </main>
    </div>
  )
}

export default Dashboard