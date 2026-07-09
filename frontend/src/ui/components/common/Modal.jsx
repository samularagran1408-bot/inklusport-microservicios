import React from 'react';

export default function Modal({ children, open = false }) {
  if (!open) return null;
  return <div className="modal">{children}</div>;
}
