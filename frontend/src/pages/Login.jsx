// src/pages/Login.jsx
import React, { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'

const Login = () => {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    try {
      const result = await login({ email, password })
      if (result.success) {
        navigate('/dashboard')
      } else {
        setError(result.error)
      }
    } catch (err) {
      setError('Error al iniciar sesión')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-page">
      <div className="auth-container">
        <div className="auth-card animate-fadeIn">
          <div className="auth-header">
            <h1 className="auth-title">PERFORMANCE PORTAL</h1>
            <p className="auth-subtitle">Iniciar Sesión</p>
            <p className="auth-description">Bienvenido de nuevo a la arquitectura del éxito.</p>
          </div>

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

            <div className="form-group">
              <div className="password-header">
                <label className="form-label">CONTRASEÑA</label>
                <Link to="/forgot-password" className="forgot-link">
                  ¿Olvidaste tu contraseña?
                </Link>
              </div>
              <input
                type="password"
                className="form-input"
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                disabled={loading}
              />
            </div>

            {error && <div className="auth-error animate-shake">{error}</div>}

            <button
              type="submit"
              className="auth-btn"
              disabled={loading}
            >
              {loading ? 'Cargando...' : 'ACCEDER'}
            </button>
          </form>

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