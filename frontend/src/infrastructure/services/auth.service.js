import { login, logout } from '../api/auth.api';

export const signIn = (creds) => login(creds);
export const signOut = () => logout();

export default { signIn, signOut };
