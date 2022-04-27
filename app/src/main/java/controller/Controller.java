package controller;
 
import controller.customerController.CustomersController;
import controller.elevatorSystemController.ElevatorSystemController; 
import connector.clientServer.Server;
import connector.clientServer.SocketCompactData;
import connector.clientServer.SocketEventListener;
import connector.protocol.ProtocolMessage; 
import lombok.Setter;
import model.Model;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
 
/**
 * Main controller .
 *
 * @see CustomersController
 * @see ElevatorSystemController
 */
public class Controller {
    public final ElevatorSystemController ELEVATOR_SYSTEM_CONTROLLER;
    private final CustomersController CUSTOMER_CONTROLLER;
    public final Model MODEL; 
    private final Logger LOGGER = Logger.getLogger(Server.class.getName()); 
    private final int TPS = 50;
    @Setter
    private Server server;

    @Setter
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
            tickControllers((long) (deltaTime));
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

    @Override
    public void onReceiveSocket(ProtocolMessage message) {
        LOGGER.info("RECIEVED MESSAGE");
    }

    @Override
    public void onNewSocketConnection(SocketCompactData message) {
        LOGGER.info("RECIEVED CONNECTION");
    }
}
