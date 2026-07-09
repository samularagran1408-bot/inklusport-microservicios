import apiClient from './client';

export const fetchSports = async () => apiClient.get('/sports');
export const getSport = async (id) => apiClient.get(`/sports/${id}`);

export default { fetchSports, getSport };
