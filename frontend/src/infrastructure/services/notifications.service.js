import { fetchNotifications } from '../api/notifications.api';

export const getNotifications = () => fetchNotifications();

export default { getNotifications };
