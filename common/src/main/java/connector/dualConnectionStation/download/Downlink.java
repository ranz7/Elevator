package connector.dualConnectionStation.download;

import connector.protocol.ProtocolMessage;

/**
 * Used to define object that can listen
 */
public interface Downlink {
    void onReceiveSocket(ProtocolMessage message);
    void onNewSocketConnection(SocketCompactData message);
}
