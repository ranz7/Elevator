package controller;

import connector.clientServer.ConnectionSettings;
import connector.clientServer.SocketCompactData;
import connector.clientServer.SocketEventListener;
import connector.clientServer.Server;
import connector.protocol.SettingsData;
import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
import controller.customerController.CustomersController;
import controller.elevatorSystemController.ElevatorSystemController;
import lombok.Setter;
import model.Model;
import common.Timer;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Main controller .
 *
 * @see CustomersController
 * @see ElevatorSystemController
 */
public class Controller implements SocketEventListener {
    private final Logger LOGGER = Logger.getLogger(Controller.class.getName());
    private final CustomersController CUSTOMER_CONTROLLER;
    public final ElevatorSystemController elevatorSystemController;
    public final Model model;

    private final LinkedList<ProtocolMessage> MESSAGES = new LinkedList<>();
    private final Timer TIMER_TO_CHECK_SERVER = new Timer();
    private final int TPS = 50;

    @Setter
    private double gameSpeed = 1;
    @Setter
    private Server server;
    private long currentTime;

    @Override
    public void onReceiveSocket(ProtocolMessage message) {
        synchronized (MESSAGES) {
            MESSAGES.add(message);
        }
    }

    @Override
    public void onNewSocketConnection(SocketCompactData client) {
        var message =
                new ProtocolMessage(Protocol.APPLICATION_SETTINGS,
                        new SettingsData(elevatorSystemController.SETTINGS, CUSTOMER_CONTROLLER.CUSTOMERS_SETTINGS,
                                gameSpeed),
                        currentTime);
        server.Send(client, message);
    }

    public Controller(Model model) {
        this.model = model;
        this.elevatorSystemController = new ElevatorSystemController(this);
        this.CUSTOMER_CONTROLLER = new CustomersController(this);
        server = new Server(this);
    }

    public void start() {
        currentTime = System.currentTimeMillis();
        if (server != null) {
            server.start();
        }


        while (true) {
            long deltaTime = System.currentTimeMillis() - currentTime;
            currentTime += deltaTime;

            writeAndReadServerStream(deltaTime);
            tickControllers((long) (deltaTime * gameSpeed));
            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeAndReadServerStream(long deltaTime) {
        if (server == null) {
            return;
        }
        TIMER_TO_CHECK_SERVER.tick(deltaTime);
        if (TIMER_TO_CHECK_SERVER.isReady()) {
            server.Send(new ProtocolMessage(Protocol.UPDATE_DATA, model.getDataToSent(), currentTime));
            TIMER_TO_CHECK_SERVER.restart(Math.round(1000. / ConnectionSettings.SSPS));
        }

        synchronized (MESSAGES) {
            MESSAGES.forEach(this::processMessageFromClient);
            MESSAGES.clear();
        }
    }

    private void tickControllers(long deltaTime) {
        CUSTOMER_CONTROLLER.tick(deltaTime);
        elevatorSystemController.tick(deltaTime);
        model.clearDead();
    }


    @SuppressWarnings("unchecked")
    private void processMessageFromClient(ProtocolMessage protocolMessage) {
        switch (protocolMessage.protocol()) {
            case CREATE_CUSTOMER -> {
                LinkedList<Integer> floors = (LinkedList<Integer>) protocolMessage.data();
                CUSTOMER_CONTROLLER.CreateCustomer(floors.get(1), floors.get(0));
            }
            case CHANGE_ELEVATORS_COUNT -> {
                elevatorSystemController.changeElevatorsCount((boolean) protocolMessage.data());
                var message =
                        new ProtocolMessage(Protocol.APPLICATION_SETTINGS,
                                new SettingsData(elevatorSystemController.SETTINGS,
                                        CUSTOMER_CONTROLLER.CUSTOMERS_SETTINGS, gameSpeed),

                                currentTime);
                server.Send(message);
            }
            case CHANGE_GAME_SPEED -> {
                gameSpeed *= (double) protocolMessage.data();
                var message = new ProtocolMessage(Protocol.CHANGE_GAME_SPEED, gameSpeed, currentTime);
                server.Send(message);
            }
        }

    }

    public void Send(Protocol protocol, Serializable data) {
        if (server == null) {
            return;
        }
        server.Send(new ProtocolMessage(protocol, data, currentTime));
    }

}
