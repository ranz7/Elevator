package connector.baseStation;

import connector.protocol.ProtocolMessage;


/*
* Interface that can send info XD
 */
public interface Downlink {
    void send(ProtocolMessage message);
}
