package connector.dualConnectionStation.download;

import connector.protocol.ProtocolMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Class in thread read stream and return object to the event listener
 *
 * @see Downlink
 */

@RequiredArgsConstructor
public class SocketStreamReader extends Thread {
    private final Socket socket;
    private final Downlink downlink;

    @Override
    @SneakyThrows
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            while (true) {
                ProtocolMessage message = new ProtocolMessage((ProtocolMessage.PureData) objectInputStream.readObject());
                downlink.onReceiveSocket(message);
                if (message == null) {
                    break;
                }
            }
        } catch (Exception exception) {
            socket.close();
             //exception.printStackTrace();
        }
    }
}
