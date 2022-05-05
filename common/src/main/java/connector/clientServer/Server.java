package connector.uplink;

import config.ConnectionSettings;
import connector.uplink.downlink.SocketCompactData;
import connector.uplink.downlink.SocketEventListener;
import connector.uplink.downlink.StreamReader;
import connector.protocol.ProtocolMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
    @Setter
    private SocketEventListener socketEventListener;

    private final LinkedList<SocketCompactData> connectedClients = new LinkedList<>();
    private final Logger LOGGER = Logger.getLogger(Server.class.getName());

    @Override
    public void run() {
        if (socketEventListener == null) {
            LOGGER.info("No listener set. ");
            return;
        }
        try {
            var serverSocket = new ServerSocket(ConnectionSettings.PORT);
            while (true) {
                LOGGER.info("wait until  new connection");
                Socket clientSocket = serverSocket.accept();
                var objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                var socketCompactData = new SocketCompactData(objectOutputStream, clientSocket);
                synchronized (connectedClients) {
                    connectedClients.add(socketCompactData);
                }
                TimeUnit.MILLISECONDS.sleep(200);
                var streamReader = new StreamReader(clientSocket, socketEventListener);
                streamReader.start();

                TimeUnit.MILLISECONDS.sleep(200);
                socketEventListener.onNewSocketConnection(socketCompactData);
            }
        } catch (IOException exception) {
            LOGGER.warning("Server exception: " + exception.getMessage());
        } catch (InterruptedException ignored) {
        }
    }

    public void Send(ProtocolMessage message) {
        synchronized (connectedClients) {
            connectedClients.removeIf(SocketCompactData::isClosed);
            for (SocketCompactData client : connectedClients) {
                Send(client, message);
            }
        }
    }

    public void Send(SocketCompactData client, ProtocolMessage message) {
        try {
            client.stream().writeObject(message.toSerializable());
        } catch (IOException ignore) {
        }
    }

}
