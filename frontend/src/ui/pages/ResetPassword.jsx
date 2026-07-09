import React from 'react'
import { useParams, Link } from 'react-router-dom'
import ResetPasswordForm from '../components/auth/ResetPasswordForm'

const ResetPassword = () => {
  const { token } = useParams()

  return (
    <div className="auth-page">
      <div className="auth-container">
        <div className="auth-card animate-fadeIn">
          <div className="auth-header">
            <h1 className="auth-title">RESTABLECER CONTRASEÑA</h1>
            <p className="auth-description">
              Ingresa tu nueva contraseña para completar el proceso.
            </p>
          </div>

          <ResetPasswordForm token={token} />

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

export default ResetPassword