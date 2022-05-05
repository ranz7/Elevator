package connector.filtersAndScenarios;

import connector.protocol.Protocol;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/*
 * This is a list of filters that will be activated in order. The last Filter will be applied
 *  until new scenario is set
 */
public class FilterScenarios {
    public static final List<Function<Protocol, Boolean>> catchSettingsThenUpdateThenAnything = new LinkedList<>(
            Arrays.asList(Filters.catchOnlySettings, Filters.catchOnlyUpdate, Filters.noFilter));

    public static final List<Function<Protocol, Boolean>> noFilter = new LinkedList<>(Arrays.asList(Filters.noFilter));
}
