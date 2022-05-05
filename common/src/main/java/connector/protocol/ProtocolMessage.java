package connector.protocol;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * All data, that is sent via socket object stream, is stored by this class.
 */

public class ProtocolMessage {
    private final PureData pureData;

    public Protocol getProtocolInMessage() {
        return pureData.protocol;
    }

    public Serializable getDataInMessage() {
        return pureData.data;
    }

    public Serializable getTimeStumpInMessage() {
        return pureData.timeStump;
    }

    public Serializable toSerializable() {
        pureData.setTimeStump(System.currentTimeMillis());
        return pureData;
    }

    public ProtocolMessage(Protocol protocol, Serializable data) {
        pureData = new PureData(protocol, data);
    }

    @RequiredArgsConstructor
    private class PureData implements Serializable {
        private final Protocol protocol;
        private final Serializable data;

        @Setter
        private long timeStump;
    }

}
