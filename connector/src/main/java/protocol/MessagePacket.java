package protocol;

import java.io.Serializable;
import java.util.List;

public record MessagePacket(List<Serializable> messages) implements Serializable {
}
