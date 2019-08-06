/**
 * The global state selectors
 */

import { createSelector } from 'reselect';
import { initialState } from './reducer';

const selectGlobal = state => state.global || initialState;

const selectRouter = state => state.router;

const makeSelectUserWrapper = () =>
  createSelector(
    selectGlobal,
    globalState => globalState.userWrapper,
  );

const makeSelectLoggedIn = () =>
  createSelector(
    selectGlobal,
    globalState => globalState.loggedIn,
  );

const makeSelectLocation = () =>
  createSelector(
    selectRouter,
    routerState => routerState.location,
  );

export {
  selectGlobal,
  makeSelectUserWrapper,
  makeSelectLocation,
  makeSelectLoggedIn,
};
