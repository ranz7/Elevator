package connector.clientServer;

import connector.protocol.ProtocolMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.Socket;
import java.util.logging.Logger;

/**
 * Class in thread read stream and return object to the event listener
 *
 * @see SocketEventListener
 */

@RequiredArgsConstructor
public class StreamReader extends Thread {
    private final Socket SOCKET; // SOCKET TO LISTEN
    private final SocketEventListener SOCKET_EVENT_LISTENER; // LISTENER OF THE SOCKET
    private final Logger LOGGER = Logger.getLogger(StreamReader.class.getName());

    @Override
    @SneakyThrows
    public void run() {
        LOGGER.info("UNCOMPLETED CLASS");
    }
}
