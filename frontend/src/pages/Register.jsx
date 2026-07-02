import React, { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'

const Register = () => {
  const [formData, setFormData] = useState({
    nombre: '',
    email: '',
    password: '',
    confirmPassword: ''
  })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { register } = useAuth()
  const navigate = useNavigate()

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')

    if (!formData.nombre || formData.nombre.trim().length === 0) {
      setError('El nombre completo es obligatorio')
      return
    }

    if (formData.nombre.trim().length < 3) {
      setError('El nombre debe tener al menos 3 caracteres')
      return
    }

    if (formData.password !== formData.confirmPassword) {
      setError('Las contraseñas no coinciden')
      return
    }

    if (formData.password.length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres')
      return
    }

    setLoading(true)

    try {
      const result = await register({
        nombre: formData.nombre.trim(),
        email: formData.email.trim(),
        password: formData.password
      })

      if (result.success) {
        navigate('/dashboard')
      } else {
        setError(result.error)
      }
    } catch (err) {
      console.error('Error en registro:', err)
      setError('Error al registrar usuario')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-page">
      <div className="auth-container">
        <div className="auth-card animate-fadeIn">
          <div className="auth-header">
            <h1 className="auth-title">REDEFINE TU POTENCIAL</h1>
            <p className="auth-subtitle">
              Plataforma de alto rendimiento para atletas adaptativos impulsada por IA.
            </p>
          </div>

          <form className="auth-form" onSubmit={handleSubmit}>
            <div className="form-group">
              <label className="form-label">NOMBRE COMPLETO</label>
              <input
                type="text"
                name="nombre"          
                className="form-input"
                placeholder="Tu nombre"
                value={formData.nombre}
                onChange={handleChange}
                required
                disabled={loading}
              />
            </div>

            <div className="form-group">
              <label className="form-label">CORREO ELECTRÓNICO</label>
              <input
                type="email"
                name="email"           
                className="form-input"
                placeholder="nombre@ejemplo.com"
                value={formData.email}
                onChange={handleChange}
                required
                disabled={loading}
              />
            </div>

            <div className="form-group">
              <label className="form-label">CONTRASEÑA</label>
              <input
                type="password"
                name="password"        
                className="form-input"
                placeholder="Mínimo 6 caracteres"
                value={formData.password}
                onChange={handleChange}
                required
                disabled={loading}
              />
            </div>

            <div className="form-group">
              <label className="form-label">CONFIRMAR CONTRASEÑA</label>
              <input
                type="password"
                name="confirmPassword" 
                className="form-input"
                placeholder="Repite tu contraseña"
                value={formData.confirmPassword}
                onChange={handleChange}
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
              {loading ? 'Registrando...' : 'CREAR CUENTA'}
            </button>
          </form>

          <div className="auth-footer">
            <p>
              ¿Ya tienes cuenta?{' '}
              <Link to="/login" className="auth-link">
                <strong>Inicia Sesión</strong>
              </Link>
            </p>
          </div>

          <div className="auth-social">
            <span className="auth-divider">O CONTINÚA CON</span>
            <div className="social-buttons">
              <button className="social-btn social-google">Google</button>
              <button className="social-btn social-apple">Apple ID</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Register