import { fetchSports, getSport } from '../api/sports.api';

export const listSports = () => fetchSports();
export const getSportById = (id) => getSport(id);

export default { listSports, getSportById };
