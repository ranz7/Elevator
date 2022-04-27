package connector.clientServer;

import java.io.ObjectOutputStream;
import java.net.Socket;

public record SocketCompactData(ObjectOutputStream stream, Socket socket) {
    public boolean isClosed() {
        return socket.isClosed();
    }
}

