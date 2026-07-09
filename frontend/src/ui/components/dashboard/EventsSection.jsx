// src/ui/components/dashboard/EventsSection.jsx
import React from 'react'

const events = [
  { date: '12', title: 'Entrenamiento Maratón Adaptado', location: 'Oficial Central, Parque A' },
  { date: '18', title: 'Abierto de Tenis en Silla', location: 'Club Deportivo de la Ciudad' },
  { date: '24', title: 'Taller Tech Biomecánica', location: 'Laboratorios Inklusport' }
]

const EventsSection = () => {
  return (
    <section className="events-section">
      <div className="section-header">
        <h2>PRÓXIMOS EVENTOS</h2>
      </div>
      <div className="events-list">
        {events.map((event, index) => (
          <div key={index} className="event-item">
            <div className="event-date">{event.date}</div>
            <div className="event-info">
              <h4>{event.title}</h4>
              <p>{event.location}</p>
            </div>
            <button className="event-link">Ver Detalles →</button>
          </div>
        ))}
      </div>
    </section>
  )
}

export default EventsSection