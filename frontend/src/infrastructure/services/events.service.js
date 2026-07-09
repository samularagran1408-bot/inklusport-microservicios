import { fetchEvents, getEvent } from '../api/events.api';

export const listEvents = () => fetchEvents();
export const getEventById = (id) => getEvent(id);

export default { listEvents, getEventById };
