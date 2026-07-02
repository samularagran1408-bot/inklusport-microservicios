import React from 'react'

const MessageInput = ({ value, onChange, onSend, disabled }) => {
  const handleKeyDown = (event) => {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault()
      onSend()
    }
  }

  return (
    <div className="message-input-container">
      <textarea
        className="message-input"
        placeholder="Escribe tu mensaje..."
        value={value}
        onChange={(e) => onChange(e.target.value)}
        onKeyDown={handleKeyDown}
        disabled={disabled}
        rows={2}
      />
      <button
        type="button"
        className="btn-send"
        onClick={onSend}
        disabled={disabled || !value.trim()}
      >
        Enviar
      </button>
    </div>
  )
}

export default MessageInput
