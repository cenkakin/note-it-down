import { defineMessages } from 'react-intl';

export default defineMessages({
  networkError: {
    id: 'noteitdown.request.network.error.message',
    defaultMessage: 'Please check your internet connection and try again',
  },
  serverError: {
    id: 'noteitdown.request.server.error.message',
    defaultMessage: 'Internal Server Error',
  },
  timeoutError: {
    id: 'noteitdown.request.timeout.error.message',
    defaultMessage:
      'Looks like the server is taking to long to respond, ' +
      'please try again in sometime',
  },
  unauthorizedError: {
    id: 'noteitdown.request.unauthorizedError.error.message',
    defaultMessage: 'Unauthorized Error!',
  },
});
