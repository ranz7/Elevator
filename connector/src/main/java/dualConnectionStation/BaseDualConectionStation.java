package dualConnectionStation;

import dualConnectionStation.download.Downlink;
import dualConnectionStation.download.Reader;
import dualConnectionStation.upload.Uplink;
import lombok.Setter;
import protocol.ProtocolMessage;

import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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

    protected AtomicBoolean isDisconnect = new AtomicBoolean(true);

    public abstract void start();

    public abstract void flush();

    public abstract boolean isDisconnect();

    public void send(Socket receiver, ProtocolMessage message) {
        var bufferedMessages = streamBuffer.get(receiver);
        if (bufferedMessages == null) {
            bufferedMessages = streamBuffer.put(receiver, new LinkedList<>());
        }
        bufferedMessages.add(message);
    }

    Map<Socket, List<ProtocolMessage>> streamBuffer = new HashMap<>();
}
