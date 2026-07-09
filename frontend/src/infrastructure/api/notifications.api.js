import apiClient from './client';

export const fetchNotifications = async () => apiClient.get('/notifications');

export default { fetchNotifications };
