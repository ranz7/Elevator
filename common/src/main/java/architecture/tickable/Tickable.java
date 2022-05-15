package architecture.tickable;

public interface Tickable {
    default void tick(double deltaTime) {
    }
}
