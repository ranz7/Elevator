package model;

import architecture.tickable.Tickable;

import java.util.List;

public interface Model {
    void clearDead();

    List<Tickable> getTickableOjects();
}
