package connector.filtersAndScenarios;

import connector.protocol.Protocol;

import java.security.cert.CertPathBuilder;
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

    public Scenario build(Function<Protocol, Boolean> filterToRepeatAtTheEnd) {
        filterSequence.add(filterToRepeatAtTheEnd);
        return new Scenario(filterSequence, onStart,onEnd);
    }
}
