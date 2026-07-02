import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../contexts/AuthContext'

const Header = () => {
  const { user, logout, isAuthenticated } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <header className="header">
      <nav className="nav">
        <div className="nav-left">
          <Link to="/" className="logo">
            Inklusport
          </Link>
        </div>
        <div className="nav-right">
          {isAuthenticated ? (
            <>
              <Link to="/dashboard" className="nav-link">Dashboard</Link>
              <Link to="/sports" className="nav-link">Deportes</Link>
              <Link to="/events" className="nav-link">Eventos</Link>
              <Link to="/chat" className="nav-link">Chatbot</Link>
              <Link to="/profile" className="nav-link">
                {user?.name || 'Perfil'}
              </Link>
              <button onClick={handleLogout} className="btn-logout">
                Cerrar Sesión
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="nav-link">Iniciar Sesión</Link>
            </>
          )}
        </div>
      </nav>
    </header>
  )
}

export default Header