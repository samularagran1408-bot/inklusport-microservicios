export const initialAuthState = { user: null, token: null };

export default function authReducer(state = initialAuthState, action) {
  switch (action.type) {
    case 'SET_USER':
      return { ...state, user: action.payload };
    case 'SET_TOKEN':
      return { ...state, token: action.payload };
    default:
      return state;
  }
}
