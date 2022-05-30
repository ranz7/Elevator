package controller;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

public abstract class ControllerEndlessLoop {
    @Getter
    @Setter
    private double controllerTimeSpeed = 1;
    @Setter
    long currentTime;

    private final TickableList objectsToTick = new TickableList();

    void start(Runnable additionalMetodToRun) {
        currentTime = System.currentTimeMillis();

        while (true) {
            long deltaTime = System.currentTimeMillis() - currentTime;
            currentTime += deltaTime;
            objectsToTick.tick(deltaTime * controllerTimeSpeed);

            additionalMetodToRun.run();

            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(1000. / getTickPerSecond()));
            } catch (InterruptedException ignore) {
            }
        }
    }

    protected void multiplyControllerSpeedBy(double multiply) {
        controllerTimeSpeed *= multiply;
    }

    protected void addTickable(Tickable tickable) {
        objectsToTick.add(tickable);
    }

    protected void start() {
        start(() -> {
        });
    }

    protected abstract int getTickPerSecond();
}
