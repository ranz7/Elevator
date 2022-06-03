package controller;

public interface Tickable {
    default void tick(double deltaTime) {
    }
}
