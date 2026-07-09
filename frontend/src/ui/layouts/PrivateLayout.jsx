import React from 'react';
import { Header } from '../components/common/index.js';

export default function PrivateLayout({ children }) {
  return (
    <div>
      <Header />
      <div className="private-content">{children}</div>
    </div>
  );
}
