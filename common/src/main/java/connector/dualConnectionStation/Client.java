package connector.dualConnectionStation;

import configs.ConnectionSettings;
import connector.dualConnectionStation.download.SocketCompactData;
import connector.dualConnectionStation.download.SocketStreamReader;
import connector.protocol.ProtocolMessage;
import lombok.Setter;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
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

public class Client extends BaseDualConectionStation {
    private ObjectOutputStream objectOutputStream;

    private Socket serversSocket;
    private SocketStreamReader socketStreamReader;
    @Setter
    private String host = ConnectionSettings.HOST;

    @Override
    public boolean isDisconnect() {
        if (serversSocket == null) {
            return true;
        }
        return isDisconnect.get() || (!socketStreamReader.isAlive());
    }

    @Override
    public void start() {
        while (serversSocket == null) {
            serversSocket = connectToServer();
        }
        socketStreamReader = new SocketStreamReader(serversSocket, downlink);
        socketStreamReader.start();
        downlink.onNewSocketConnection(new SocketCompactData(objectOutputStream, serversSocket));
        Logger.getAnonymousLogger().info("Connected +");
        isDisconnect.set(false);
        return;
    }

    private Socket connectToServer() {
        Socket newServerSocket;
        try {
            newServerSocket = new Socket(host, ConnectionSettings.PORT);
            objectOutputStream = new ObjectOutputStream(newServerSocket.getOutputStream());
        } catch (ConnectException ignored) {
            Logger.getAnonymousLogger().info("Connecting . . .");
            try {
                TimeUnit.MILLISECONDS.sleep(1500); // reconnect time
            } catch (InterruptedException ignored2) {
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return newServerSocket;

    }

    public void send(ProtocolMessage message) {
        try {
            objectOutputStream.writeObject(message.toSerializable());
        } catch (IOException ignored) {
        }
    }
}
