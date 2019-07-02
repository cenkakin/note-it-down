/*
 * HomeReducer
 *
 * The reducer takes care of our data. Using actions, we can
 * update our application state. To add a new action,
 * add it to the switch statement in the reducer function
 *
 */

import produce from 'immer';
import { CHANGE_EMAIL, CHANGE_PASSWORD } from './constants';

export const initialState = {
  email: '',
  password: '',
};

/* eslint-disable default-case, no-param-reassign */
const homeReducer = (state = initialState, action) =>
  produce(state, draft => {
    switch (action.type) {
      case CHANGE_EMAIL:
        draft.email = action.email;
        console.log(action.email);
        break;
      case CHANGE_PASSWORD:
        draft.password = action.password;
        console.log(action.password);
        break;
    }
  });

export default homeReducer;
