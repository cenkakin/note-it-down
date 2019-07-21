/* eslint-disable */
import 'whatwg-fetch';
import apisauce from 'apisauce';
import { call } from 'redux-saga/effects';

import messages from './messages';

const create = () => {
  const api = apisauce.create({
    baseURL: 'http://localhost:8762/',
    'Cache-Control': 'no-cache',
    'X-Device-Type': 'NORMAL',
  });

  const get = (url, parameters = {}) => executeRequest(() => api.get(url, parameters));
  const post = (url, object = {}) => executeRequest(() => api.post(url, object));
  const put = (url, object = {}) => executeRequest(() => api.put(url, object));
  const remove = (url, parameters = {}) => executeRequest(() => api.delete(url, parameters));

  const executeRequest = (req) => new Promise((resolve) => {
    resolve(req().then((res) => {
      return { ...res, errorMessage: getErrorMessage(res) };
    }));
  });

  return {
    get,
    post,
    put,
    remove,
  };
};

const getErrorMessage = (res) => {
  let errorMessage;
  if (res.ok) {

  } else {
    errorMessage = getErrorMessageWhenSystemFails(res);
  }
  return errorMessage;
};

const getErrorMessageWhenSystemFails = (res) => {
  let errorMessage;
  if (res.problem === 'NETWORK_ERROR' || res.problem === 'CONNECTION_ERROR') {
    errorMessage = messages.networkError;
  } else if (res.problem === 'SERVER_ERROR') {
    errorMessage = messages.serverError;
  } else if (res.problem === 'TIMEOUT_ERROR') {
    errorMessage = messages.timeoutError;
  } else if (res.status === 401) {
    errorMessage = messages.unauthorizedError;
  }
  return errorMessage;
};

export const apiCall = create();

export function* doGetRequest(url, params) {
  return yield call(doRequest, apiCall.get, url, params);
}

export function* doPostRequest(url, params) {
  return yield call(doRequest, apiCall.post, url, params);
}

export function* doPutRequest(url, params) {
  return yield call(doRequest, apiCall.put, url, params);
}

export function* doDeleteRequest(url, params) {
  return yield call(doRequest, apiCall.remove, url, params);
}

function* doRequest(method, url, params) {
  return yield call(method, url, params);
}
