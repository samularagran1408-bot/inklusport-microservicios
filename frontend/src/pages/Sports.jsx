import React from 'react'
import Header from '../components/common/Header'

const Sports = () => {
  return (
    <div className="page">
      <Header />
      <main className="sports-container">
        <h1>Deportes</h1>
        <p>Descubre los deportes adaptados disponibles.</p>
        <div className="sports-grid">
          <div className="sport-card">
            <h3>Natación Adaptada</h3>
            <p>Para personas con movilidad reducida</p>
          </div>
          <div className="sport-card">
            <h3>Baloncesto en Silla</h3>
            <p>Competitivo y divertido</p>
          </div>
          <div className="sport-card">
            <h3>Yoga Inclusivo</h3>
            <p>Para todos los niveles</p>
          </div>
          <div className="sport-card">
            <h3>Atletismo Adaptado</h3>
            <p>Con prótesis o asistencias</p>
          </div>
        </div>
      </main>
    </div>
  )
}

export default Sports