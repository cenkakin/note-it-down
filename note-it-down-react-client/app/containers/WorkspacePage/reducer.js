import produce from 'immer';
import { LOAD_WORKSPACE_SUCCESS } from './constants';

export const initialState = {
	workspace: '',
};

/* eslint-disable default-case, no-param-reassign */
const workspaceReducer = (state = initialState, action) =>
  produce(state, draft => {
    switch (action.type) {
      case LOAD_WORKSPACE_SUCCESS:
        draft.workspace = action.workspace;
        break;
    }
  });

export default workspaceReducer;
