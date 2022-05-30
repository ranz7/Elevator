package dualConnectionStation.upload;

import protocol.ProtocolMessage;

import java.net.Socket;
import java.util.List;


/*
* Interface that can send info XD
 */
public interface Uplink {
    void send(Socket receiver,ProtocolMessage message);
    List<Socket> getReceivers();
}
