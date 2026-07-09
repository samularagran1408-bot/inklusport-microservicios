import React from 'react'

const sports = [
  { id: 1, name: 'Baloncesto en Silla de Ruedas', category: 'ALTA INTENSIDAD - EN EQUIPO', event: 'Par-Natación', detail: 'RESISTENCIA - INDIVIDUAL' },
  { id: 2, name: 'Atletismo Adaptado', category: 'SPRINT - PRECISIÓN', event: 'Competencia Nacional', detail: 'VELOCIDAD - RESISTENCIA' },
  { id: 3, name: 'Para-Ciclismo', category: 'AL AIRE LIBRE - FUERZA', event: 'Ruta Inklusport', detail: 'RESISTENCIA - VELOCIDAD' }
]

const SportsSection = () => {
  return (
    <section className="sports-section">
      <div className="section-header">
        <h2>DESCUBRE TU DISCIPLINA</h2>
        <div className="filter-tabs">
          <button className="filter-tab active">Todas</button>
          <button className="filter-tab">Paralímpico</button>
          <button className="filter-tab">Recreativo</button>
          <button className="filter-tab">Deportes de España</button>
        </div>
      </div>
      <div className="sports-grid">
        {sports.map((sport) => (
          <div key={sport.id} className="sport-card">
            <div className="sport-category">{sport.category}</div>
            <h3>{sport.name}</h3>
            <p className="sport-event">{sport.event}</p>
            <p className="sport-location">{sport.detail}</p>
            <button className="sport-link">Ver Detalles →</button>
          </div>
        ))}
      </div>
    </section>
  )
}

export default SportsSection