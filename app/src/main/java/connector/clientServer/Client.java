package connector.clientServer;

import connector.protocol.ProtocolMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * This class can connect to the Server,
 * get all connection information from the Connection Settings class
 *
 * @see ConnectionSettings
 * @see Server
 */

@RequiredArgsConstructor
public class Client {
    private final SocketEventListener SOCKET_EVENT_LISTENER;
    @Setter
    private String host = ConnectionSettings.HOST;
    private final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public void reconnect() {
        LOGGER.info("UNCOMPLETED CLASS");
    }

}
