package connector.dualConnectionStation.upload;

import connector.protocol.ProtocolMessage;


/*
* Interface that can send info XD
 */
public interface Uplink {
    void send(ProtocolMessage message);
}
