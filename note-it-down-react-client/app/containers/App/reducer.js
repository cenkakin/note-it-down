import produce from 'immer';
import {
  LOAD_REPOS,
  LOAD_REPOS_ERROR,
  LOAD_REPOS_SUCCESS,
  LOGGED_IN, LOGGED_OUT,
} from './constants';
import { getUser } from '../../utils/storage';

// The initial state of the App

const user = getUser();

export const initialState = {
  loggedIn: !!user,
  loading: false,
  error: false,
  user: user || {},
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
        draft.user = action.user;
        break;
      case LOGGED_OUT:
        draft.loggedIn = false;
        draft.user = {};
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
