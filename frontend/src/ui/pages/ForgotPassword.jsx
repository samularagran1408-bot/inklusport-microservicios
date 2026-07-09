import React from 'react'
import { Link } from 'react-router-dom'
import ForgotPasswordForm from '../components/auth/ForgotPasswordForm'

const ForgotPassword = () => {
  return (
    <div className="auth-page">
      <div className="auth-container">
        <div className="auth-card animate-fadeIn">
          <div className="auth-header">
            <h1 className="auth-title">RECUPERAR CONTRASEÑA</h1>
            <p className="auth-description">
              Ingresa tu correo electrónico y te enviaremos un enlace para restablecer tu contraseña.
            </p>
          </div>

          <ForgotPasswordForm />

          <div className="auth-footer">
            <p>
              <Link to="/login" className="auth-link">
                ← Volver al inicio de sesión
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ForgotPassword