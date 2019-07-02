/**
 * SignIn selectors
 */

import { createSelector } from 'reselect';
import { initialState } from './reducer';

const selectSignIn = state => state.signin || initialState;

const makeSelectEmail = () =>
  createSelector(
    selectSignIn,
    signin => signin.email,
  );

const makeSelectPassword = () =>
  createSelector(
    selectSignIn,
    signin => signin.password,
  );

export { selectSignIn, makeSelectEmail, makeSelectPassword };
