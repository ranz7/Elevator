package protocol;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.List;

public record MessagePacket(ProtocolMessage.PureData[] messages) implements Serializable {
}
