import apiClient from './client';

export const fetchAccessibilitySettings = async () => apiClient.get('/accessibility');

export default { fetchAccessibilitySettings };
