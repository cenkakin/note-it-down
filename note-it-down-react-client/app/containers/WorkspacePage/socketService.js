import { getUserWrapper } from '../../utils/storage';
import { WS_BASE_URL } from '../../config';
import syncStatuses from '../../components/Editor/syncStatuses';

export class SocketService {
  init() {
    const user = getUserWrapper();
    this.ws = new WebSocket(
      `${WS_BASE_URL}/note/websocket/note?token=${user.token}&subject=${
        user.user.email
      }&id=${user.user.id}`,
    );

    this.syncStatus = syncStatuses.synced;
    this.lastContent = null;
    return this;
  }

  send(content) {
    if (content === this.lastContent) {
      return;
    }
    const data = this.createWSEvent(content);
    this.ws.send(JSON.stringify(data));
    this.syncStatus = syncStatuses.onSaving;
    this.lastContent = content;
  }

  onMessage() {
    this.ws.onmessage = event => {
      const processedMessage = JSON.parse(event);
      if (this.lastTransactionId === processedMessage) {
        this.syncStatus = syncStatuses.synced;
      }
    };
  }

  createWSEvent(content) {
    const transactionId = new Date().getTime();
    this.lastTransactionId = transactionId;
    return { transactionId, content };
  }
}
