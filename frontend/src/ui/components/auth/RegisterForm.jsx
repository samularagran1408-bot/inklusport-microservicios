import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../../domain/contexts/AuthContext'

const RegisterForm = () => {
  const [formData, setFormData] = useState({
    nombre: '',
    email: '',
    telefono: '',
    disabilityType: '',
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

    if (!formData.nombre || formData.nombre.trim().length < 3) {
      setError('El nombre completo es obligatorio (mínimo 3 caracteres)')
      return
    }

    if (!formData.email || !formData.email.includes('@')) {
      setError('El correo electrónico es obligatorio')
      return
    }

    if (formData.password.length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres')
      return
    }

    if (formData.password !== formData.confirmPassword) {
      setError('Las contraseñas no coinciden')
      return
    }

    setLoading(true)

    try {
      const result = await register({
        nombre: formData.nombre.trim(),
        email: formData.email.trim(),
        telefono: formData.telefono,
        disabilityType: formData.disabilityType,
        password: formData.password
      })

      if (result.success) {
        navigate('/dashboard')
      } else {
        setError(result.error)
      }
    } catch (err) {
      setError('Error al registrar usuario')
    } finally {
      setLoading(false)
    }
  }

  return (
    <form className="auth-form" onSubmit={handleSubmit}>
      <div className="form-group">
        <label className="form-label">NOMBRE COMPLETO</label>
        <input
          type="text"
          name="nombre"
          className="form-input"
          placeholder="Juan Pérez"
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
          placeholder="juan@ejemplo.com"
          value={formData.email}
          onChange={handleChange}
          required
          disabled={loading}
        />
      </div>

      <div className="form-group">
        <label className="form-label">TELÉFONO</label>
        <input
          type="tel"
          name="telefono"
          className="form-input"
          placeholder="+34 000 000 000"
          value={formData.telefono}
          onChange={handleChange}
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

      <button type="submit" className="auth-btn" disabled={loading}>
        {loading ? 'Registrando...' : 'REGISTRARSE'}
      </button>
    </form>
  )
}

export default RegisterForm