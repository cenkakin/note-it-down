/* eslint-disable */
import 'whatwg-fetch';
import apisauce from 'apisauce';
import { call } from 'redux-saga/effects';

import messages from './messages';
import { getUser } from './storage';

const create = () => {
  const apiSauce = apisauce.create({
    baseURL: 'http://localhost:8762/',
		headers: {
    	'Access-Control-Allow-Origin': '*',
		}
  });

  const get = (url, parameters = {}) => executeRequest(() => apiSauce.get(url, parameters));
  const post = (url, object = {}) => executeRequest(() => apiSauce.post(url, object));
  const put = (url, object = {}) => executeRequest(() => apiSauce.put(url, object));
  const remove = (url, parameters = {}) => executeRequest(() => apiSauce.delete(url, parameters));
  const setToken = (token) => apiSauce.setHeader('Authorization', token);
  const removeToken = () => apiSauce.deleteHeader('Authorization');

	const user = getUser();

	if(user && user.token) {
		setToken(user.token);
	}

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
		setToken,
    removeToken,
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

export const api = create();

export function* doGetRequest(url, params) {
  return yield call(doRequest, api.get, url, params);
}

export function* doPostRequest(url, params) {
  return yield call(doRequest, api.post, url, params);
}

export function* doPutRequest(url, params) {
  return yield call(doRequest, api.put, url, params);
}

export function* doDeleteRequest(url, params) {
  return yield call(doRequest, api.remove, url, params);
}

function* doRequest(method, url, params) {
  return yield call(method, url, params);
}
