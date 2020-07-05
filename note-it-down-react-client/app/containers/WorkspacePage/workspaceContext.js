import { createContext } from 'react';
import { SocketService } from './socketService';

export const WorkspaceContext = createContext(new SocketService());
