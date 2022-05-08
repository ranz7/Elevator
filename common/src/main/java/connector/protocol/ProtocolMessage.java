package connector.protocol;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * All data, that is sent via socket object stream, is stored by this class.
 */

@AllArgsConstructor
public class ProtocolMessage {
    private final PureData pureData;

    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class PureData implements Serializable {
        private final Protocol protocol;
        private final Serializable data;

        @Setter
        private long timeStump;
    }

    public ProtocolMessage(Protocol protocol, Serializable data) {
        pureData = new PureData(protocol, data);
    }

    public Protocol getProtocolInMessage() {
        return pureData.protocol;
    }

    public Serializable getDataInMessage() {
        return pureData.data;
    }

    public long getTimeStumpInMessage() {
        return pureData.timeStump;
    }

    public Serializable toSerializable() {
        pureData.setTimeStump(System.currentTimeMillis());
        return new PureData(pureData.protocol, pureData.data, pureData.timeStump);
    }
}
