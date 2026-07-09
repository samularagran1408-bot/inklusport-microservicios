import React, { useState } from 'react'
import { Link } from 'react-router-dom'
import { authApi } from '../../../infrastructure/api/auth.api'

const ForgotPasswordForm = () => {
  const [email, setEmail] = useState('')
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const [sent, setSent] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setMessage('')
    setLoading(true)

    try {
      await authApi.forgotPassword(email)
      setSent(true)
      setMessage('Se ha enviado un enlace de recuperación a tu correo electrónico.')
    } catch (err) {
      setError('Error al enviar el correo de recuperación. Verifica tu email.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <>
      {sent ? (
        <div className="success-message">
          <p>{message}</p>
          <Link to="/login" className="auth-link">
            <strong>Volver al inicio de sesión</strong>
          </Link>
        </div>
      ) : (
        <form className="auth-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">CORREO ELECTRÓNICO</label>
            <input
              type="email"
              className="form-input"
              placeholder="nombre@ejemplo.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              disabled={loading}
            />
          </div>

          {error && <div className="auth-error animate-shake">{error}</div>}

          <button type="submit" className="auth-btn" disabled={loading}>
            {loading ? 'Enviando...' : 'ENVIAR ENLACE DE RECUPERACIÓN'}
          </button>
        </form>
      )}
    </>
  )
}

export default ForgotPasswordForm