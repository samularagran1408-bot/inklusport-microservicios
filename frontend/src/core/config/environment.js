const ENV = import.meta.env

export const API_CONFIG = {
  GATEWAY: ENV.VITE_API_GATEWAY || 'http://localhost:8080',
  AUTH: ENV.VITE_API_AUTH || 'http://localhost:3001',
  USERS: ENV.VITE_API_USERS || 'http://localhost:3002',
  SPORTS: ENV.VITE_API_SPORTS || 'http://localhost:3003',
  ACCESSIBILITY: ENV.VITE_API_ACCESSIBILITY || 'http://localhost:3004',
  AI: ENV.VITE_API_AI || 'http://localhost:3008',
}

export const APP_CONFIG = {
  NAME: 'Inklusport',
  VERSION: '2.0.0',
  DEFAULT_THEME: 'dark',
  SUPPORTED_LANGUAGES: ['es', 'en'],
  DISABILITY_TYPES: ['visual', 'motriz', 'cognitiva', 'auditiva', 'multiple'],
}