import { createSelector } from 'reselect';
import { initialState } from './reducer';

const selectLogin = state => state.login || initialState;

const makeSelectEmail = () =>
  createSelector(
    selectLogin,
    loginState => loginState.email,
  );

const makeSelectPassword = () =>
  createSelector(
    selectLogin,
    loginState => loginState.password,
  );

const makeSelectLoginError = () =>
  createSelector(
    selectLogin,
    loginState => loginState.loginError,
  );

export { selectLogin, makeSelectEmail, makeSelectPassword, makeSelectLoginError };
