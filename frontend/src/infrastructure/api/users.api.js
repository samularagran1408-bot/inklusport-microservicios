import apiClient from './client';

export const fetchUsers = async () => apiClient.get('/users');
export const getUser = async (id) => apiClient.get(`/users/${id}`);

export default { fetchUsers, getUser };
