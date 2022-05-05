package connector;

import configs.ConnectionSettings;
import connector.baseStation.BaseStation;
import connector.baseStation.download.SocketCompactData;
import connector.baseStation.download.SocketStreamReader;
import connector.protocol.ProtocolMessage;
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

public class Client extends BaseStation {
    private ObjectOutputStream objectOutputStream;

    private Socket serversSocket;
    private SocketStreamReader socketStreamReader;
    @Setter
    private String host = ConnectionSettings.HOST;

    @Override
    public boolean isStopped() {
        if (serversSocket == null) {
            return true;
        }
        return !socketStreamReader.isAlive();
    }

    @Override
    public void start() {
        while (true) {
            try {
                serversSocket = new Socket(host, ConnectionSettings.PORT);
                objectOutputStream = new ObjectOutputStream(serversSocket.getOutputStream());

                socketStreamReader = new SocketStreamReader(serversSocket, uplink);
                socketStreamReader.start();
                uplink.onNewSocketConnection(new SocketCompactData(objectOutputStream, serversSocket));
                isStoped.set(true);
                Logger.getAnonymousLogger().info("Connected +");
                return;
            } catch (IOException ignored) {
            }
            try {
                Logger.getAnonymousLogger().info("Connecting . . .");
                TimeUnit.MILLISECONDS.sleep(1500); // reconnect time
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void send(ProtocolMessage message) {
        try {
            objectOutputStream.writeObject(message.toSerializable());
        } catch (IOException ignored) {
        }
    }
}
