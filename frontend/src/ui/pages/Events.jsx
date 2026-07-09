// src/pages/Events.jsx
import React from 'react'
import Header from '../components/common/Header'

const Events = () => {
  return (
    <div className="page">
      <Header />
      <main className="events-container">
        <h1>Eventos</h1>
        <p>Próximos eventos inclusivos.</p>
        <div className="events-list">
          <div className="event-card">
            <h3>Clínica de Natación</h3>
            <p>15 de Julio, 2026</p>
            <p>Coliseo Municipal</p>
            <button className="btn-register">Inscribirse</button>
          </div>
          <div className="event-card">
            <h3>Torneo de Baloncesto</h3>
            <p>20 de Julio, 2026</p>
            <p>Gimnasio Inklusport</p>
            <button className="btn-register">Inscribirse</button>
          </div>
        </div>
      </main>
    </div>
  )
}

export default Events