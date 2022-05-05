package connector.baseStation.download;

import connector.protocol.ProtocolMessage;

/**
 * Used to define object that can listen
 */
public interface Uplink {
    void onReceiveSocket(ProtocolMessage message);
    void onNewSocketConnection(SocketCompactData message);
}
