import { put, takeLatest } from 'redux-saga/effects';
import { LOAD_WORKSPACE } from './constants';
import { doGetRequest } from '../../utils/request';
import { workspaceLoaded } from './actions';

export function* getWorkspace() {
  const res = yield doGetRequest('/workspace/');
  if (res.ok) {
    yield put(workspaceLoaded(res.data));
  } else {
    yield put(workspaceLoaded(res.data));
  }
}

export default function* workspaceData() {
  yield takeLatest(LOAD_WORKSPACE, getWorkspace);
}
