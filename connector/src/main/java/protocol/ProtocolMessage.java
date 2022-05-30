package protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.net.Socket;

/**
 * All data, that is sent via socket object stream, is stored by this class.
 */

@RequiredArgsConstructor
public class ProtocolMessage {
    @Getter
    @Setter
    private Socket owner;

    private final PureData pureData;


    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class PureData implements Serializable {
        private final Protocol protocol;
        private final int roomId;
        private final Serializable data;
        @Setter
        private long timeStump;
    }

    public ProtocolMessage(Protocol protocol, int worldId, Serializable data) {
        pureData = new PureData(protocol, worldId, data);
    }

    public Protocol getProtocol() {
        return pureData.protocol;
    }

    public Serializable getData() {
        return pureData.data;
    }

    public long getTimeStump() {
        return pureData.timeStump;
    }

    public int getRoomId() {
        return pureData.roomId;
    }

    public Serializable toSerializable() {
        pureData.setTimeStump(System.currentTimeMillis());
        return new PureData(pureData.protocol, pureData.roomId, pureData.data, pureData.timeStump);
    }
}
