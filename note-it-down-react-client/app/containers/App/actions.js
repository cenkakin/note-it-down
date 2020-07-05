import { LOGGED_IN, LOGGED_OUT } from './constants';

export function successfulLogin(userWrapper) {
  return {
    type: LOGGED_IN,
    userWrapper,
  };
}

export function logOut() {
  return {
    type: LOGGED_OUT,
  };
}
