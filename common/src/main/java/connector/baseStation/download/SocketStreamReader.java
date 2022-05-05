package connector.baseStation.download;

import connector.protocol.ProtocolMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Class in thread read stream and return object to the event listener
 *
 * @see Uplink
 */

@RequiredArgsConstructor
public class SocketStreamReader extends Thread {
    private final Socket socket;
    private final Uplink uplink;

    @Override
    @SneakyThrows
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            while (true) {
                ProtocolMessage message = new ProtocolMessage((ProtocolMessage.PureData) objectInputStream.readObject());
                uplink.onReceiveSocket(message);
                if (message == null) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException exception) {
            socket.close();
            exception.printStackTrace();
        }
    }
}
