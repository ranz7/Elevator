package connector.clientServer;

import connector.protocol.ProtocolMessage;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Clients connects to the Server, with usage of connection settings.
 *
 * @see Client
 * @see ConnectionSettings
 */

@RequiredArgsConstructor
public class Server extends Thread {
    private final SocketEventListener SOCKET_EVENT_LISTENER;
    private final LinkedList<SocketCompactData> CONNECTED_CLIENTS = new LinkedList<>();
    private final Logger LOGGER = Logger.getLogger(Server.class.getName());

    @Override
    public void run() {
        LOGGER.info("UNCOMPLETED CLASS");
    }

}
