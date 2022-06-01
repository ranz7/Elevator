package dualConnectionStation.download;

import dualConnectionStation.BaseDualConectionStation;
import dualConnectionStation.upload.Uplink;
import protocol.MessagePacket;
import protocol.ProtocolMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * Class in thread read stream and return object to the event listener
 *
 * @see Downlink
 */

@RequiredArgsConstructor
public class SocketStreamReader extends Thread {
    private final Socket socket;
    private final Downlink downlink;
    private boolean isClosed = true;

    public boolean isClosed() {
        return isClosed;
    }

    @Override
    @SneakyThrows
    public void run() {
        try {
            isClosed = false;
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            while (true) {
                var messages = ((MessagePacket) objectInputStream.readObject()).messages();
                if (messages == null) {
                    break;
                }
                for (int i = 0; i < messages.length; i++) {
                    var message = new ProtocolMessage(messages[i]);
                    message.setOwner(socket);
                    downlink.onReceiveMessage(message);
                }
            }
        } catch (Exception exception) {
            socket.close();
            isClosed = true;
            downlink.onLostSocketConnection(socket);
            exception.printStackTrace();
        }
    }
}
