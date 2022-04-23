package controller;

import lombok.Setter;
import model.Model;
import java.util.concurrent.TimeUnit;

public class Controller {
    public final Model MODEL;

    private final int TPS = 50;

    @Setter
    private double gameSpeed = 1;
    private long currentTime;

    public Controller(Model model) {
        this.MODEL = model;
    }

    public void start() {
        currentTime = System.currentTimeMillis();

        while (true) {
            long deltaTime = System.currentTimeMillis() - currentTime;
            currentTime += deltaTime;

            tickControllers((long) (deltaTime * gameSpeed));
            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void tickControllers(long deltaTime) {
       // controllers tick tu
        MODEL.clearDead();
    }
}
