package controller;

import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@NoArgsConstructor
public class TickableList implements Tickable {
    private List<Tickable> tickables = new LinkedList<>();
    private final List<Supplier<List<? extends Tickable>>> tickablesDynamic = new LinkedList<>();

    public TickableList(Collection<? extends Tickable> tickableList) {
        tickables.addAll(tickableList);
    }

    public TickableList add(Tickable tickable) {
        tickables.add(tickable);
        return this;
    }

    public TickableList add(TickableList tickableList) {
        tickables.addAll(tickableList.tickables);
        return this;
    }

    public TickableList add(List<? extends Tickable> tickableList) {
        tickables.addAll(tickableList);
        return this;
    }

    public TickableList add(Supplier<List<? extends Tickable>> dynamicCreatedTickables) {
        tickablesDynamic.add(dynamicCreatedTickables);
        return this;
    }

    @Override
    public void tick(double deltaTime) {
        tickables.forEach(tickable -> tickable.tick(deltaTime));
        tickablesDynamic.forEach(
                tickablesDynamic -> tickablesDynamic.get()
                        .forEach(tickable -> tickable.tick(deltaTime)));
    }
}
