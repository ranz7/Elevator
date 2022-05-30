package dualConnectionStation.download;

import java.io.ObjectOutputStream;
import java.net.Socket;

public record Reader(ObjectOutputStream stream, Socket socket) {
    public boolean isClosed() {
        return socket.isClosed();
    }
}

