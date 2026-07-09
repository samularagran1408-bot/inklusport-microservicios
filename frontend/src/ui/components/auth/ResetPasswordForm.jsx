import React, { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { authApi } from '../../../infrastructure/api/auth.api'

const ResetPasswordForm = ({ token }) => {
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const [reset, setReset] = useState(false)
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setMessage('')

    if (password.length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres')
      return
    }

    if (password !== confirmPassword) {
      setError('Las contraseñas no coinciden')
      return
    }

    setLoading(true)

    try {
      await authApi.resetPassword({ token, password })
      setReset(true)
      setMessage('Contraseña restablecida exitosamente.')
      setTimeout(() => navigate('/login'), 3000)
    } catch (err) {
      setError('Error al restablecer la contraseña. El enlace puede haber expirado.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <>
      {reset ? (
        <div className="success-message">
          <p>✅ {message}</p>
          <p>Serás redirigido al inicio de sesión...</p>
        </div>
      ) : (
        <form className="auth-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">NUEVA CONTRASEÑA</label>
            <input
              type="password"
              className="form-input"
              placeholder="Mínimo 6 caracteres"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label className="form-label">CONFIRMAR CONTRASEÑA</label>
            <input
              type="password"
              className="form-input"
              placeholder="Repite tu nueva contraseña"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
              disabled={loading}
            />
          </div>

          {error && <div className="auth-error animate-shake">{error}</div>}

          <button type="submit" className="auth-btn" disabled={loading}>
            {loading ? 'Actualizando...' : 'RESTABLECER CONTRASEÑA'}
          </button>
        </form>
      )}
    </>
  )
}

export default ResetPasswordForm