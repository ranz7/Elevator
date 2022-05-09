package connector.filtersAndScenarios;

import connector.protocol.Protocol;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/*
 * This is a list of filters that will be activated in order. The last Filter will be applied
 *  until new scenario is set
 */
@AllArgsConstructor
public class Scenario {
    private final List<Function<Protocol, Boolean>> filterSequence;
    private Runnable onEnd;
    private Runnable onStart;

    public void pop() {
        if (onStart != null) {
            onStart.run();
            onStart = null;
        }
        if (filterSequence.size() != 1) {
            filterSequence.remove(0);

        } else {
            if (onEnd != null) {
                onEnd.run();
                onEnd = null;
            }
        }
    }

    public Function<Protocol, Boolean> get() {
        return filterSequence.get(0);
    }
}
