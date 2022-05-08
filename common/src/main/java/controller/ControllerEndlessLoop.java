package controller;

import architecture.tickable.TickableList;
import lombok.Getter;
import lombok.Setter;
import model.Model;
import architecture.tickable.Tickable;

import java.util.concurrent.TimeUnit;

public class ControllerEndlessLoop {
    @Getter
    @Setter
    private double controllerSpeed = 1;
    @Setter
    long currentTime;

    private final TickableList objectsToTick = new TickableList();
    private Model model = null;

    void start(Runnable additionalMetodToRun) {
        currentTime = System.currentTimeMillis();
        if(model!=null) {
            model.start();
        }

        while (true) {
            long deltaTime = System.currentTimeMillis() - currentTime;
            currentTime += deltaTime;

            objectsToTick.tick(deltaTime * controllerSpeed);

            additionalMetodToRun.run();

            if (model != null) {
                new TickableList().add(model.getTickableList()).tick(deltaTime * controllerSpeed);
                model.clearDead();
            }
            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(1000. / getTickPerSecond()));
            } catch (InterruptedException ignore) {
            }
        }
    }

    protected void multiplyControllerSpeedBy(double multiply) {
        controllerSpeed *= multiply;
    }

    void addModel(Model model) {
        this.model = model;
    }

    void addTickable(Tickable tickable) {
        objectsToTick.add(tickable);
    }

    void start() {
        start(() -> {
        });
    }

    int getTickPerSecond() {
        return 1;
    }
}
