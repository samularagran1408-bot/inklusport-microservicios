import React, { useRef, useEffect } from 'react'

const MessageList = ({ messages }) => {
  const containerRef = useRef(null)

  useEffect(() => {
    if (containerRef.current) {
      containerRef.current.scrollTop = containerRef.current.scrollHeight
    }
  }, [messages])

  if (messages.length === 0) {
    return (
      <div className="message-list-empty">
        <p>¡Hola! Soy el asistente de Inklusport. ¿Cómo puedo ayudarte?</p>
        <p className="subtitle">Pregúntame sobre deportes, eventos o inscripciones.</p>
      </div>
    )
  }

  return (
    <div className="message-list" ref={containerRef}>
      {messages.map((msg, index) => (
        <div
          key={index}
          className={`message ${msg.sender === 'user' ? 'user' : 'bot'}`}
        >
          <div className="message-bubble">
            <span className="message-sender">
              {msg.sender === 'user' ? 'Tú' : 'Asistente'}
            </span>
            <p className="message-text">{msg.text}</p>
            <span className="message-time">
              {new Date(msg.timestamp).toLocaleTimeString()}
            </span>
          </div>
        </div>
      ))}
    </div>
  )
}

export default MessageList