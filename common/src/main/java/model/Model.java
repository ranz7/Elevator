package model;

import architecture.tickable.TickableList;

public interface Model {
    void clearDead();

    TickableList getTickableList();
}
