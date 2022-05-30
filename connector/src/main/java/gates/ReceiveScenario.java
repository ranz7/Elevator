package gates;
import lombok.AllArgsConstructor;
import protocol.Protocol;

import java.util.List;
import java.util.function.Function;

/*
 * This is a list of filters that will be activated in order. The last Filter will be applied
 *  until new scenario is set
 */
@AllArgsConstructor
public class ReceiveScenario {
    private final List<Function<Protocol, Boolean>> filterSequence;
    private Runnable onStart;
    private Runnable onEnd;

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
