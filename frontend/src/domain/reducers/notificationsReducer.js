export const initialNotificationsState = { items: [] };

export default function notificationsReducer(state = initialNotificationsState, action) {
  switch (action.type) {
    case 'SET_NOTIFICATIONS':
      return { ...state, items: action.payload };
    case 'ADD_NOTIFICATION':
      return { ...state, items: [action.payload, ...state.items] };
    default:
      return state;
  }
}
