package connector;

import config.ConnectionSettings;
import connector.baseStation.BaseStation;
import connector.baseStation.download.SocketCompactData;
import connector.baseStation.download.SocketStreamReader;
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
public class Server  extends BaseStation {
    private final LinkedList<SocketCompactData> connectedClients = new LinkedList<>();


    public void start(){
        new Thread(() -> {
            isStoped.set(false);

            if (uplink == null) {
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
                    var streamReader = new SocketStreamReader(clientSocket, uplink);
                    streamReader.start();

                    TimeUnit.MILLISECONDS.sleep(200);
                    uplink.onNewSocketConnection(socketCompactData);
                }
            } catch (IOException exception) {
                Logger.getAnonymousLogger().warning("Server exception: " + exception.getMessage());
            } catch (InterruptedException ignored) {
            } finally {
                isStoped.set(true);
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
    public boolean isStopped() {
        return isStoped.get();
    }

    private void send(SocketCompactData client, ProtocolMessage message) {
        try {
            client.stream().writeObject(message.toSerializable());
        } catch (IOException ignore) {
        }
    }
}
