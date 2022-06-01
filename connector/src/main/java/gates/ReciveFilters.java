package gates;

import protocol.Protocol;

import java.util.function.Function;

public class ReciveFilters {
    public static Function<Protocol, Boolean> noFilter() {
        return (protocol -> false);
    }
    public static Function<Protocol, Boolean> catchOnlySettings() {
        return protocol -> protocol != Protocol.ROOMS_PREPARE_SETTINGS;
    }
    public static Function<Protocol, Boolean> catchOnlyUpdate() {
        return protocol -> protocol != Protocol.UPDATE_DATA;
    }

    public static Function<Protocol, Boolean> catchOnlyHello() {
        return protocol -> protocol != Protocol.HELLO_MESSAGE;
    }

}
