import produce from 'immer';
import { LOGGED_IN, LOGGED_OUT } from './constants';
import { getUserWrapper } from '../../utils/storage';

// The initial state of the App

const userWrapper = getUserWrapper();

export const initialState = {
  loggedIn: !!userWrapper,
  loading: false,
  error: false,
  userWrapper: userWrapper || {},
  userData: {
    repositories: false,
  },
};

/* eslint-disable default-case, no-param-reassign */
const appReducer = (state = initialState, action) =>
  produce(state, draft => {
    switch (action.type) {
      case LOGGED_IN:
        draft.loggedIn = true;
        draft.userWrapper = action.userWrapper;
        break;
      case LOGGED_OUT:
        draft.loggedIn = false;
        draft.userWrapper = {};
        break;
    }
  });

export default appReducer;
