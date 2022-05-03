package connector.clientServer;

import connector.protocol.ProtocolMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Class in thread read stream and return object to the event listener
 *
 * @see SocketEventListener
 */

@RequiredArgsConstructor
public class StreamReader extends Thread {
    private final Socket SOCKET;
    private final SocketEventListener SOCKET_EVENT_LISTENER;
    private final Logger LOGGER = Logger.getLogger(StreamReader.class.getName());

    @Override
    @SneakyThrows
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(SOCKET.getInputStream());

            while (true) {
                ProtocolMessage message = (ProtocolMessage) objectInputStream.readObject();
                SOCKET_EVENT_LISTENER.onReceiveSocket(message);
                if (message == null) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException exception) {
            SOCKET.close();
            LOGGER.info("Disconnected");
//            exception.printStackTrace();
        }
    }
}
