package gates;

import protocol.Protocol;
import protocol.ProtocolMessage;
import protocol.special.SubscribeRequest;

import java.io.Serializable;
import java.util.function.Function;

public class SendFilters {
    public static Function<ProtocolMessage, Boolean> sendOnlyIfSubscribed(SubscribeRequest request) {
        return protocolMessage -> (!request.roomsToSubscribeFor().contains(protocolMessage.getRoomId()))
                || (protocolMessage.getRoomId() == -1);
    }
}
