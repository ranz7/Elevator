package dualConnectionStation.download;

import dualConnectionStation.BaseDualConectionStation;
import dualConnectionStation.upload.Uplink;
import protocol.MessagePacket;
import protocol.ProtocolMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.ObjectInputStream;
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
            isClosed = true;
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            while (true) {
                var data = (MessagePacket) objectInputStream.readObject();
                if (data == null) {
                    break;
                }
                data.messages().forEach(pureData -> {
                    ProtocolMessage message = new ProtocolMessage((ProtocolMessage.PureData) pureData);
                    message.setOwner(socket);
                    downlink.onReceiveMessage(message);
                });
            }
        } catch (Exception exception) {
            socket.close();
            isClosed = false;
            downlink.onLostSocketConnection(socket);
            //   exception.printStackTrace();
        }
    }
}
