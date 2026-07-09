import React from 'react'
import { Link } from 'react-router-dom'
import LoginForm from '../components/auth/LoginForm'

const Login = () => {
  return (
    <div className="auth-page">
      <div className="auth-container">
        <div className="auth-card animate-fadeIn">
          <div className="auth-header">
            <h1 className="auth-title">PERFORMANCE PORTAL</h1>
            <p className="auth-subtitle">Iniciar Sesión</p>
            <p className="auth-description">
              Bienvenido de nuevo a la arquitectura del éxito.
            </p>
          </div>

          <LoginForm />

          <div className="auth-social">
            <span className="auth-divider">O CONTINÚA CON</span>
            <div className="social-buttons">
              <button className="social-btn social-google">Google</button>
              <button className="social-btn social-apple">Apple ID</button>
            </div>
          </div>

          <div className="auth-footer">
            <p>
              ¿No tienes cuenta?{' '}
              <Link to="/register" className="auth-link">
                <strong>Regístrate</strong>
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Login