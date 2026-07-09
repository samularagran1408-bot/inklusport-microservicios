import React from 'react'

const AuthLayout = ({ children, title, subtitle, description }) => {
  return (
    <div className="auth-page">
      <div className="auth-container">
        <div className="auth-card animate-fadeIn">
          <div className="auth-header">
            <h1 className="auth-title">{title || 'INKLUSPORT'}</h1>
            {subtitle && <p className="auth-subtitle">{subtitle}</p>}
            {description && <p className="auth-description">{description}</p>}
          </div>

          {children}

          <div className="auth-footer">
            <p>
              ¿Ya tienes cuenta?{' '}
              <a href="/login" className="auth-link">
                <strong>Inicia Sesión</strong>
              </a>
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default AuthLayout