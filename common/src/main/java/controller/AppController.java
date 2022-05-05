package controller;

import connector.Bridge;
import connector.uplink.Server;
import config.InitializationSettingsForClient;
import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
import connector.protocol.ProtocolMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import model.Model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Main controller .
 *
 * @see CustomersController
 * @see ElevatorSystemController
 */

@RequiredArgsConstructor
public class AppController extends GameLoop implements ProtocolMessageListener {
    private final CustomersController customerController = new CustomersController(this);
    public final ElevatorSystemController elevatorSystemController = new ElevatorSystemController(this);
    public final Bridge bridge = new Bridge(new Server(), this);

    public final Model model;
    private final int TPS = 50;
    private double gameSpeed = 1;
    private long currentTime;


    public void start() {
        currentTime = System.currentTimeMillis();
        bridge.setOnConnectionMessage(() -> new ProtocolMessage(
                Protocol.APPLICATION_SETTINGS,
                new InitializationSettingsForClient(
                        elevatorSystemController.SETTINGS,
                        customerController.CUSTOMERS_SETTINGS,
                        gameSpeed)));
        bridge.start();

        while (true) {
            long deltaTime = System.currentTimeMillis() - currentTime;
            currentTime += deltaTime;

            tickControllers((long) (deltaTime * gameSpeed));
            bridge.tick(deltaTime);

            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void tickControllers(long deltaTime) {
        customerController.tick(deltaTime);
        elevatorSystemController.tick(deltaTime);
        model.clearDead();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean popMessage(Protocol protocol, Serializable data) {
        switch (protocol) {
            case CREATE_CUSTOMER -> {
                LinkedList<Integer> floors = (LinkedList<Integer>) data;
                customerController.CreateCustomer(floors.get(1), floors.get(0));
            }
            case CHANGE_ELEVATORS_COUNT -> {
                elevatorSystemController.changeElevatorsCount((boolean) data);
                bridge.Send(Protocol.APPLICATION_SETTINGS, new InitializationSettingsForClient(
                        elevatorSystemController.SETTINGS, customerController.CUSTOMERS_SETTINGS, gameSpeed));
            }
            case CHANGE_GAME_SPEED -> {
                gameSpeed *= (double) data;
                bridge.Send(Protocol.CHANGE_GAME_SPEED, gameSpeed);
            }
        }
        return true;
    }

    public void Send(Protocol protocol, Serializable data) {
        bridge.Send(protocol, data);
    }
}
