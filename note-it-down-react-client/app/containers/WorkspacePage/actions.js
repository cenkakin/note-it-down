import { LOAD_WORKSPACE, LOAD_WORKSPACE_SUCCESS } from './constants';

export function loadWorkspace() {
  return {
    type: LOAD_WORKSPACE,
  };
}

export function workspaceLoaded(workspace) {
  return {
    type: LOAD_WORKSPACE_SUCCESS,
    workspace,
  };
}
