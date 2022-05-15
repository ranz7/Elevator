package controller;

import architecture.tickable.TickableList;
import lombok.Getter;
import lombok.Setter;
import architecture.tickable.Tickable;

import java.util.concurrent.TimeUnit;

public abstract class ControllerEndlessLoop {
    @Getter
    @Setter
    private double controllerTimeSpeed = 1;
    @Setter
    long currentTime;

    private final TickableList objectsToTick = new TickableList();
    private CollectionOfDeadObjects model = null;

    void start(Runnable additionalMetodToRun) {
        currentTime = System.currentTimeMillis();
        if (model != null) {
            model.start();
        }

        while (true) {
            long deltaTime = System.currentTimeMillis() - currentTime;
            currentTime += deltaTime;
            objectsToTick.tick(deltaTime * controllerTimeSpeed);

            additionalMetodToRun.run();

            if (model != null) {
                new TickableList().add(model.getTickableList()).tick(deltaTime * controllerTimeSpeed);
                model.clearDead();
            }
            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(1000. / getTickPerSecond()));
            } catch (InterruptedException ignore) {
            }
        }
    }

    protected void multiplyControllerSpeedBy(double multiply) {
        controllerTimeSpeed *= multiply;
    }

    protected void addModel(CollectionOfDeadObjects model) {
        this.model = model;
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
