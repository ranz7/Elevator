package dualConnectionStation;

import configs.ConnectionSettings;
import dualConnectionStation.download.Reader;
import dualConnectionStation.download.SocketStreamReader;
import protocol.MessagePacket;
import protocol.ProtocolMessage;
import lombok.Setter;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    boolean connecting = false;


    @Override
    public boolean isDisconnected() {
        if (serversSocket == null) {
            return true;
        }
        return (socketStreamReader == null || !socketStreamReader.isAlive() || socketStreamReader.isClosed());
    }

    @Override
    public boolean isConnecting() {
        return connecting;
    }

    @Override
    public void changeIp() {
        try {
            if(serversSocket!=null) {
                serversSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(Objects.equals(host, ConnectionSettings.HOST)){
            host = ConnectionSettings.HOST2;
        }else{
            host = ConnectionSettings.HOST;
        }
    }

    @Override
    public void start() {
        connecting = true;
        new Thread(() -> {
            int attempts = ConnectionSettings.attempts;
            serversSocket = null;
            while (serversSocket == null) {
                Logger.getAnonymousLogger().info("ATTEMPT: " + (ConnectionSettings.attempts - attempts) +
                        " of " + (ConnectionSettings.attempts - 1));
                serversSocket = connectToServer();
                attempts--;
                if (attempts == 0) {
                    connecting = false;
                    downlink.onLostSocketConnection(serversSocket);
                    return;
                }
            }
            socketStreamReader = new SocketStreamReader(serversSocket, downlink);
            socketStreamReader.start();
            downlink.onNewSocketConnection(serversSocket);
            Logger.getAnonymousLogger().info("Connected +");
            connecting = false;
        }).start();
    }

    @Override
    public void flush() {
        if (isDisconnected()) {
            streamBuffer.clear();
            return;
        }
        var messagesToServer = streamBuffer.get(serversSocket);
        if (messagesToServer == null) {
            return;
        }
        try {
            ProtocolMessage.PureData[] messages = new ProtocolMessage.PureData[messagesToServer.size()];
            for (int i = 0; i < messages.length; i++) {
            //    Logger.getAnonymousLogger().info("SENT : " + messagesToServer.get(i).getProtocol());
                messages[i] = messagesToServer.get(i).toPureData();
            }
            objectOutputStream.writeObject(new MessagePacket(messages));
        } catch (IOException e) {
            e.printStackTrace();
        }
        streamBuffer.clear();
    }

    private Socket connectToServer() {
        Socket newServerSocket;
        try {
            newServerSocket = new Socket(host, ConnectionSettings.PORT);
            objectOutputStream = new ObjectOutputStream(newServerSocket.getOutputStream());
        } catch (ConnectException ignored) {
            try {
                TimeUnit.MILLISECONDS.sleep(ConnectionSettings.reconnectTime); // reconnect time
            } catch (InterruptedException ignored2) {
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return newServerSocket;

    }


    @Override
    public List<Socket> getReceivers() {
        var answer = new LinkedList<Socket>();
        answer.add(serversSocket);
        return answer;
    }
}
