package connector.filtersAndScenarios;

import connector.protocol.Protocol;

import java.util.function.Function;

public class Filters {
    public static final Function<Protocol, Boolean> noFilter = (protocol -> false);
    public static final Function<Protocol, Boolean> catchOnlySettings = protocol -> protocol != Protocol.APPLICATION_SETTINGS;
    public static final Function<Protocol, Boolean> catchOnlyUpdate = protocol -> protocol != Protocol.UPDATE_DATA;
}
