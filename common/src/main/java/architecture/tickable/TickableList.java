package architecture.tickable;

import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@NoArgsConstructor
public class TickableList implements Tickable {
    private List<Tickable> tickables = new LinkedList<>();
    private final List<Supplier<List<Tickable>>> tickablesDynamic = new LinkedList<>();

    public TickableList(List<Tickable> tickableOjects) {
        tickables = tickableOjects;
    }

    public void add(Tickable tickable) {
        tickables.add(tickable);
    }

    public void add(Supplier<List<Tickable>> dynamicCreatedTickables) {
        tickablesDynamic.add(dynamicCreatedTickables);
    }


    public void tick(double deltaTime) {
        this.tick((long) deltaTime);
    }

    @Override
    public void tick(long deltaTime) {
        tickables.forEach(tickable -> tickable.tick(deltaTime));
        tickablesDynamic.forEach(
                tickablesDynamic -> tickablesDynamic.get()
                        .forEach(tickable -> tickable.tick(deltaTime)));
    }
}
