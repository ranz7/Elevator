package dualConnectionStation;

import dualConnectionStation.download.Downlink;
import dualConnectionStation.upload.Uplink;
import lombok.Setter;
import protocol.ProtocolMessage;

import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Base station is a class that can get write information to Downlink
 * and read information from Uplink
 *
 * @see Uplink
 * @see Downlink
 */
public abstract class BaseDualConectionStation implements Uplink {
    @Setter
    protected Downlink downlink;

    public abstract void start();

    public abstract void flush();

    public abstract boolean isDisconnected();
    public abstract boolean isConnecting();

    public void send(Socket receiver, ProtocolMessage message) {
        if(isDisconnected()){
            streamBuffer.clear();
            return;
        }
        synchronized (streamBuffer) {
            var bufferedMessages = streamBuffer.get(receiver);
            if (bufferedMessages == null) {
                streamBuffer.put(receiver, new LinkedList<>());
                bufferedMessages = streamBuffer.get(receiver);
            }
            bufferedMessages.add(message);
        }
    }

    Map<Socket, List<ProtocolMessage>> streamBuffer = new HashMap<>();
}
