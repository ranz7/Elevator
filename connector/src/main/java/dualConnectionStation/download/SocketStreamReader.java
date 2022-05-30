package dualConnectionStation.download;

import protocol.ProtocolMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.ObjectInputStream;
import java.net.Socket;

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
                var data = (ProtocolMessage.PureData) objectInputStream.readObject();
                ProtocolMessage message = new ProtocolMessage(data);
                message.setOwner(socket);
                downlink.onReceiveMessage(message);

                if (message == null) {
                    break;
                }
            }
        } catch (Exception exception) {
            socket.close();
//             exception.printStackTrace();
        }
    }
}
