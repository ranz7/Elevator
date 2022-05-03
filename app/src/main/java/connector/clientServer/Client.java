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
    private ObjectOutputStream objectOutputStream;
    private Socket serversSocket;
    private StreamReader streamReader;
    @Setter
    private String host = ConnectionSettings.HOST;

    public void reconnect() {
        while (true) {
            try {
                serversSocket = new Socket(host, ConnectionSettings.PORT);
                objectOutputStream = new ObjectOutputStream(serversSocket.getOutputStream());

                streamReader = new StreamReader(serversSocket, SOCKET_EVENT_LISTENER);
                streamReader.start();
                SOCKET_EVENT_LISTENER.onNewSocketConnection(new SocketCompactData(objectOutputStream, serversSocket));
                return;
            } catch (IOException ignored) {
            }
            try {
                TimeUnit.MILLISECONDS.sleep(400); // reconnect time
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void send(ProtocolMessage message) {
        try {
            objectOutputStream.writeObject(message);
        } catch (IOException ignored) {
        }
    }

    public boolean isClosed() {
        if (serversSocket == null) {
            return true;
        }
        return !streamReader.isAlive();
    }
}
