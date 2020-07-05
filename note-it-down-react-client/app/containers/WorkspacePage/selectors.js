/**
 * The global state selectors
 */

import { createSelector } from 'reselect';
import { initialState } from './reducer';

const selectWorkspace = state => state.workspace || initialState;

const makeSelectWorkspace = () =>
  createSelector(
    selectWorkspace,
    workspaceState => workspaceState.workspace,
  );

const makeSelectWorkspaceLoaded = () =>
  createSelector(
    selectWorkspace,
    workspaceState => workspaceState.workspaceLoaded,
  );

export { selectWorkspace, makeSelectWorkspace, makeSelectWorkspaceLoaded };
