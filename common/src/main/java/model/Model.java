package model;

import architecture.tickable.Startable;
import architecture.tickable.TickableList;
import architecture.tickable.Updatable;

public interface Model extends Startable, Updatable {
    void clearDead();

    TickableList getTickableList();
}
