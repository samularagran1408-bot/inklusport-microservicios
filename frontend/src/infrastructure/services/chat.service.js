import { sendMessage, fetchMessages } from '../api/chat.api';

export const postMessage = (payload) => sendMessage(payload);
export const getMessages = () => fetchMessages();

export default { postMessage, getMessages };
