package controller;


import controller.customerController.CustomersController;
import controller.elevatorSystemController.ElevatorSystemController;
import lombok.Setter;
import model.Model;
import java.util.concurrent.TimeUnit;

/**
 * Main controller .
 *
 * @see CustomersController
 * @see ElevatorSystemController
 */
public class Controller {
    public final ElevatorSystemController ELEVATOR_SYSTEM_CONTROLLER;
    public final Model MODEL;

    private final CustomersController CUSTOMER_CONTROLLER;
    private final int TPS = 50;

    @Setter
    private double gameSpeed = 1;
    private long currentTime;

    public Controller(Model model) {
        this.MODEL = model;
        this.ELEVATOR_SYSTEM_CONTROLLER = new ElevatorSystemController(this);
        this.CUSTOMER_CONTROLLER = new CustomersController(this);
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
        CUSTOMER_CONTROLLER.tick(deltaTime);
        ELEVATOR_SYSTEM_CONTROLLER.tick(deltaTime);
        MODEL.clearDead();
    }
}
