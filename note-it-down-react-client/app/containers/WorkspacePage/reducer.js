import produce from 'immer';
import { LOAD_WORKSPACE_SUCCESS } from './constants';

export const initialState = {
  workspace: null,
  workspaceLoaded: false,
};

/* eslint-disable default-case, no-param-reassign */
const workspaceReducer = (state = initialState, action) =>
  produce(state, draft => {
    switch (action.type) {
      case LOAD_WORKSPACE_SUCCESS:
        draft.workspace = action.workspace;
        draft.workspaceLoaded = true;
        break;
    }
  });

export default workspaceReducer;
