package dualConnectionStation;

import configs.ConnectionSettings;
import dualConnectionStation.download.Reader;
import dualConnectionStation.download.SocketStreamReader;
import protocol.MessagePacket;
import protocol.ProtocolMessage;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Clients connects to the Server, with usage of connection settings.
 *
 * @see Client
 * @see ConnectionSettings
 */

@RequiredArgsConstructor
public class Server extends BaseDualConectionStation {
    private final LinkedList<Reader> connectedClients = new LinkedList<>();
    boolean isDisconnected = true;

    public void start() {
        new Thread(() -> {
            isDisconnected = false;

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
                    var socketCompactData = new Reader(objectOutputStream, clientSocket);
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
                isDisconnected = true;
            }
        }).start();
    }

    @Override
    public void flush() {
        if(isDisconnected){
            streamBuffer.clear();
            return;
        }
        synchronized (connectedClients) {
            connectedClients.removeIf(Reader::isClosed);
            connectedClients.forEach(
                    reader -> {
                        streamBuffer.forEach(
                                (socket, protocolMessages) -> {
                                    if (reader.socket() == socket) {
                                        sendAll(reader, protocolMessages);
                                    }
                                }
                        );
                    }
            );
        }
    }

    private void sendAll(Reader client, List<ProtocolMessage> messages) {
        try {
            client.stream().writeObject(
                    new MessagePacket(
                            messages.stream().map(ProtocolMessage::toSerializable)
                                    .collect(Collectors.toList())));
        } catch (IOException ignore) {
        }
    }

    @Override
    public boolean isDisconnected() {
        return isDisconnected;
    }

    @Override
    public boolean isConnecting() {
        return false;
    }


    @Override
    public List<Socket> getReceivers() {
        synchronized (connectedClients) {
            return connectedClients.stream()
                    .map(Reader::socket)
                    .collect(Collectors.toList());
        }
    }
}
