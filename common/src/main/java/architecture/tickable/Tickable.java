package architecture.tickable;

import java.util.LinkedList;

public interface Tickable {
    default void tick(double deltaTime) {
    }

    default TickableList getTickableList() {
        var tickableList = new TickableList();
        tickableList.add(this);
        return tickableList;
    }
}
