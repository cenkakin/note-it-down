import { call, put, select, takeLatest } from 'redux-saga/effects';

import { doPostRequest } from '../../utils/request';
import { makeSelectEmail, makeSelectPassword } from './selectors';
import messages from './messages';
import { LOGIN } from './constants';
import { changeEmail, changePassword, errorOnLogin } from './actions';

export function* loginRequest() {
  const email = yield select(makeSelectEmail());
  const password = yield select(makeSelectPassword());
  const res = yield call(doPostRequest, 'auth/login', {
    username: email,
    password,
  });
  console.log(res);
  yield put(errorOnLogin(''));
  if (res.ok) {
    yield call(() =>
      localStorage.setItem('identity', JSON.stringify(identity)),
    );
    yield put(changeEmail(''));
    yield put(changePassword(''));
  } else {
    yield put(errorOnLogin(messages.invalidCredentials));
  }
}

/**
 * Root saga manages watcher lifecycle
 */
export default function* login() {
  yield takeLatest(LOGIN, loginRequest);
}
