package gates;

import protocol.Protocol;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class ScenarioBuilder {
    private final List<Function<Protocol, Boolean>> filterSequence = new LinkedList<>();
    private Runnable onEnd;
    private Runnable onStart;


    public ScenarioBuilder setEndFunction(Runnable onEnd) {
        this.onEnd = onEnd;
        return this;
    }

    public ScenarioBuilder setStartFunction(Runnable onStart) {
        this.onStart = onStart;
        return this;
    }

    public ScenarioBuilder add(Function<Protocol, Boolean> filter) {
        filterSequence.add(filter);
        return this;
    }

    public ReceiveScenario build(Function<Protocol, Boolean> filterToRepeatAtTheEnd) {
        filterSequence.add(filterToRepeatAtTheEnd);
        return new ReceiveScenario(filterSequence, onStart,onEnd);
    }
}
