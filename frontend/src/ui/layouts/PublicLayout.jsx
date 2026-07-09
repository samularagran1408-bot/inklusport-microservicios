import React from 'react';
import { Header, Footer } from '../components/common/index.js';

export default function PublicLayout({ children }) {
  return (
    <div>
      <Header />
      <main>{children}</main>
      <Footer />
    </div>
  );
}
