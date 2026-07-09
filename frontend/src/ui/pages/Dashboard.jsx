import React from 'react'
import HeroSection from '../components/dashboard/HeroSection'
import ServicesSection from '../components/dashboard/ServicesSection'
import SportsSection from '../components/dashboard/SportsSection'
import EventsSection from '../components/dashboard/EventsSection'

const Dashboard = () => {
  return (
    <div className="dashboard-page">
      <main>
        <HeroSection />
        <ServicesSection />
        <SportsSection />
        <EventsSection />
      </main>
    </div>
  )
}

export default Dashboard