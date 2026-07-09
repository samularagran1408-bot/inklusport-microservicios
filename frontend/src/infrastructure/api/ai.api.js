import apiClient from './client';

export const queryAI = async (payload) => apiClient.post('/ai/query', payload);

export default { queryAI };
