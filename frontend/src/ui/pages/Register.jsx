import React from 'react'
import { Link } from 'react-router-dom'
import RegisterForm from '../components/auth/RegisterForm'

const Register = () => {
  return (
    <div className="auth-page register-page">
      <div className="auth-container">
        <div className="auth-card animate-fadeIn">
          <div className="auth-header">
            <h1 className="auth-title">INKLUSPORT</h1>
            <p className="auth-subtitle">Crear Cuenta</p>
            <p className="auth-description">
              <span className="highlight-text">ALTO RENDIMIENTO</span>
              <br />
              Únete a la élite del deporte adaptado.
              <br />
              Tu potencial no tiene límites.
              <br />
              Registra tu perfil y comienza a competir hoy mismo.
            </p>
          </div>

          <RegisterForm />

          <div className="auth-social">
            <span className="auth-divider">O REGÍSTRATE CON</span>
            <div className="social-buttons">
              <button className="social-btn">Google</button>
              <button className="social-btn">Apple</button>
              <button className="social-btn">Facebook</button>
            </div>
          </div>

          <div className="auth-footer">
            <p>
              ¿Ya tienes una cuenta?{' '}
              <Link to="/login" className="auth-link">
                <strong>Inicia Sesión</strong>
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Register