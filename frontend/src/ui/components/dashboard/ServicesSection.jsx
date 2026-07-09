// src/ui/components/dashboard/ServicesSection.jsx
import React from 'react'

const services = [
  {
    icon: '🔬',
    title: 'Análisis Biomecánico',
    description: 'Seguimiento avanzado por sensores y modelos de IA para optimizar la eficiencia del movimiento y prevenir lesiones por fatiga.'
  },
  {
    icon: '🏋️',
    title: 'Entrenamiento Adaptado',
    description: 'Acceso directo a entrenadores certificados de élite especializados en tecnologías adaptadas y protocolos de entrenamiento modificados.'
  },
  {
    icon: '🌍',
    title: 'Ecosistema de Inclusión',
    description: 'Una red global para que los atletas conecten, compartan datos y descubran competencias inclusivas en todo el mundo.'
  }
]

const ServicesSection = () => {
  return (
    <section className="services-section">
      <div className="section-header">
        <h2>SERVICIOS ESPECIALIZADOS</h2>
        <p>Herramientas de élite para el logro adaptado.</p>
      </div>
      <div className="services-grid">
        {services.map((service, index) => (
          <div key={index} className="service-card">
            <div className="service-icon">{service.icon}</div>
            <h3>{service.title}</h3>
            <p>{service.description}</p>
          </div>
        ))}
      </div>
    </section>
  )
}

export default ServicesSection