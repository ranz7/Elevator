package connector.protocol;

import java.io.Serializable;

/**
 * All data, that is sent via socket object stream, is stored by this class.
 */

public record ProtocolMessage(Protocol protocol, Serializable data, long timeStump) implements Serializable {
}
