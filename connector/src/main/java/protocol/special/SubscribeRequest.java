package protocol.special;


import java.io.Serializable;
import java.util.List;

public record SubscribeRequest(List<Integer> roomsToSubscribeFor) implements Serializable {
}
