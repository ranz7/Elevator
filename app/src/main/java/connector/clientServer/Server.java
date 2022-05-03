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
        try {
            var serverSocket = new ServerSocket(ConnectionSettings.PORT);
            while (true) {
                LOGGER.info("wait until  new connection");
                Socket clientSocket = serverSocket.accept();
                var objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                var socketCompactData = new SocketCompactData(objectOutputStream, clientSocket);
                synchronized (CONNECTED_CLIENTS) {
                    CONNECTED_CLIENTS.add(socketCompactData);
                }
                TimeUnit.MILLISECONDS.sleep(200);
                var streamReader = new StreamReader(clientSocket, SOCKET_EVENT_LISTENER);
                streamReader.start();

                TimeUnit.MILLISECONDS.sleep(200);
                SOCKET_EVENT_LISTENER.onNewSocketConnection(socketCompactData);
            }
        } catch (IOException exception) {
            LOGGER.warning("Server exception: " + exception.getMessage());
        } catch (InterruptedException ignored) {
        }
    }

    public void Send(ProtocolMessage message) {
        synchronized (CONNECTED_CLIENTS) {
            CONNECTED_CLIENTS.removeIf(SocketCompactData::isClosed);
            for (SocketCompactData client : CONNECTED_CLIENTS) {
                Send(client, message);
            }
        }
    }

    public void Send(SocketCompactData client, ProtocolMessage message) {
        try {
            client.stream().writeObject(message);
        } catch (IOException ignore) {
        }
    }


}
