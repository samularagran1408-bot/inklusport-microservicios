import React from 'react'
import Header from '../components/common/Header'
import ChatBot from '../components/chat/ChatBot'

const ChatPage = () => {
  return (
    <div className="page">
      <Header />
      <main className="chat-page-container">
        <div className="chat-wrapper">
          <ChatBot />
        </div>
      </main>
    </div>
  )
}

export default ChatPage