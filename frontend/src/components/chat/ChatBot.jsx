import React, { useState } from 'react'
import { useChat } from '../../contexts/ChatContext'
import MessageList from './MessageList'
import MessageInput from './MessageInput'
import LoadingSpinner from '../common/LoadingSpinner'

const ChatBot = () => {
  const { messages, loading, sendMessage, error, clearChat } = useChat()
  const [inputValue, setInputValue] = useState('')

  const handleSend = async () => {
    if (!inputValue.trim()) return
    const message = inputValue.trim()
    setInputValue('')
    await sendMessage(message)
  }

  return (
    <div className="chat-container">
      <div className="chat-header">
        <h2>Asistente Inklusport</h2>
        <button onClick={clearChat} className="btn-clear">
          Limpiar
        </button>
      </div>

      <MessageList messages={messages} />

      {error && (
        <div className="chat-error">
          {error}
        </div>
      )}

      {loading && <LoadingSpinner size="small" />}

      <MessageInput
        value={inputValue}
        onChange={setInputValue}
        onSend={handleSend}
        disabled={loading}
      />
    </div>
  )
}

export default ChatBot