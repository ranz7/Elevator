package connector.dualConnectionStation;

import configs.ConnectionSettings;
import connector.dualConnectionStation.download.SocketCompactData;
import connector.dualConnectionStation.download.SocketStreamReader;
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
public class Server  extends BaseDualConectionStation {
    private final LinkedList<SocketCompactData> connectedClients = new LinkedList<>();

    public void start(){
        new Thread(() -> {
            isDisconnect.set(false);

            if (downlink == null) {
                Logger.getAnonymousLogger().info("No listener set. ");
                return;
            }
            try {
                var serverSocket = new ServerSocket(ConnectionSettings.PORT);
                while (true) {
                    Logger.getAnonymousLogger().info("Wait until  new connection");
                    Socket clientSocket = serverSocket.accept();
                    var objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    var socketCompactData = new SocketCompactData(objectOutputStream, clientSocket);
                    synchronized (connectedClients) {
                        connectedClients.add(socketCompactData);
                    }
                    TimeUnit.MILLISECONDS.sleep(200);
                    SocketStreamReader streamReader = new SocketStreamReader(clientSocket, downlink);
                    streamReader.start();

                    TimeUnit.MILLISECONDS.sleep(200);
                    downlink.onNewSocketConnection(socketCompactData);
                }
            } catch (Exception exception) {
                Logger.getAnonymousLogger().warning("Server exception: " + exception.getMessage());
            } finally {
                isDisconnect.set(true);
            }
        }).start();
    }


    public void send(ProtocolMessage message) {
        synchronized (connectedClients) {
            connectedClients.removeIf(SocketCompactData::isClosed);
            for (SocketCompactData client : connectedClients) {
                send(client, message);
            }
        }
    }

    @Override
    public boolean isDisconnect() {
        return isDisconnect.get();
    }

    private void send(SocketCompactData client, ProtocolMessage message) {
        try {
            client.stream().writeObject(message.toSerializable());
        } catch (IOException ignore) {
        }
    }
}
