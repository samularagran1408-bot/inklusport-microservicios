import React from 'react'
import { Link } from 'react-router-dom'
import Header from '../components/common/Header'

const Home = () => {
  return (
    <div className="page">
      <Header />
      <main className="home-container">
        <section className="hero">
          <h1>Inklusport</h1>
          <p className="subtitle">
            Deporte adaptado para todos
          </p>
          <p className="description">
            Descubre deportes adaptados, eventos inclusivos y una comunidad que te apoya.
          </p>
          <div className="cta-buttons">
            <Link to="/login" className="btn-primary">
              Comenzar
            </Link>
            <Link to="/sports" className="btn-secondary">
              Ver Deportes
            </Link>
          </div>
        </section>
      </main>
    </div>
  )
}

export default Home