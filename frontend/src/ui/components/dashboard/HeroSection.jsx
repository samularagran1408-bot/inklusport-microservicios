// src/ui/components/dashboard/HeroSection.jsx
import React from 'react'
import { Link } from 'react-router-dom'

const HeroSection = () => {
  return (
    <section className="hero-section">
      <div className="hero-content">
        <h1 className="hero-title">
          RENDIMIENTO <span>SIN FRONTERAS</span>
        </h1>
        <h2 className="hero-subtitle">LIBERTAD KINÉTICA</h2>
        <p className="hero-description">
          Plataforma de deportes adaptados de alto rendimiento diseñada para superar
          los límites del potencial humano. Datos de precisión, entrenadores de élite
          e inclusión total.
        </p>
        <Link to="/sports" className="hero-btn">
          EXPLORAR DEPORTES →
        </Link>
      </div>
    </section>
  )
}

export default HeroSection