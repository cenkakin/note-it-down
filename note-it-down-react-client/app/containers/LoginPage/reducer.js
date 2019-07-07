import produce from 'immer';
import { CHANGE_EMAIL, CHANGE_PASSWORD } from './constants';

export const initialState = {
  password: '',
  email: '',
  loginError: '',
};

/* eslint-disable default-case, no-param-reassign */
const loginReducer = (state = initialState, action) =>
  produce(state, draft => {
    switch (action.type) {
      case CHANGE_PASSWORD:
        draft.password = action.password;
        break;
      case CHANGE_EMAIL:
        draft.email = action.email;
        break;
    }
  });

export default loginReducer;
