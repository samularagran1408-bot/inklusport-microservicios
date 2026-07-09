import apiClient from './client';

export const fetchEvents = async () => apiClient.get('/events');
export const getEvent = async (id) => apiClient.get(`/events/${id}`);

export default { fetchEvents, getEvent };
