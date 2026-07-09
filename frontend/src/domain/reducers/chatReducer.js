export const initialChatState = { messages: [] };

export default function chatReducer(state = initialChatState, action) {
  switch (action.type) {
    case 'ADD_MESSAGE':
      return { ...state, messages: [...state.messages, action.payload] };
    case 'SET_MESSAGES':
      return { ...state, messages: action.payload };
    default:
      return state;
  }
}
