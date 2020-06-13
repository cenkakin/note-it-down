import { getUserWrapper } from '../../utils/storage';
import { WS_BASE_URL } from '../../config';

const createWs = subscribers => {
  const user = getUserWrapper();

  const ws = new WebSocket(
    `${WS_BASE_URL}/note/websocket/note?token=${user.token}&subject=${
      user.user.email
    }&id=${user.user.id}`,
  );

  ws.onmessage = event =>
    subscribers
      .filter(s => s.notifyMessage instanceof Function)
      .forEach(s => s.notifyMessage(event));

  ws.onclose = event => {
    console.warn('WebSocket is closing', event);
    subscribers
      .filter(s => s.notifyClose instanceof Function)
      .forEach(s => s.notifyClose(event));
  };

  ws.onerror = event => {
    console.error('Error happened on ws', event);
    subscribers
      .filter(s => s.notifyError instanceof Function)
      .forEach(s => s.notifyError(event));
  };

  const event = {
    transactionId: 123,
    operations: [],
  };

  ws.onopen = () => {
    console.log('Connection is successfully established!');
  };
  return ws;
};

export default createWs;
