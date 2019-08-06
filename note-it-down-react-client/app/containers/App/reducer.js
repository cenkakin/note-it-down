import produce from 'immer';
import {
  LOAD_REPOS,
  LOAD_REPOS_ERROR,
  LOAD_REPOS_SUCCESS,
  LOGGED_IN,
  LOGGED_OUT,
} from './constants';
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
      case LOAD_REPOS:
        draft.loading = true;
        draft.error = false;
        draft.userData.repositories = false;
        break;

      case LOAD_REPOS_SUCCESS:
        draft.userData.repositories = action.repos;
        draft.loading = false;
        draft.currentUser = action.username;
        break;

      case LOAD_REPOS_ERROR:
        draft.error = action.error;
        draft.loading = false;
        break;
    }
  });

export default appReducer;
