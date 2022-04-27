package connector.clientServer;

import connector.protocol.ProtocolMessage;

/**
 * Interface is used by Client and Server to inform WindowClient and Client about new events
 */
public interface SocketEventListener {
    void onReceiveSocket(ProtocolMessage message);
    void onNewSocketConnection(SocketCompactData message);
}
