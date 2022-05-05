package controller;

import config.ConnectionSettings;
import connector.Gates;
import connector.Server;
import config.InitializationSettingsForClient;
import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
import connector.protocol.ProtocolMessageListener;
import lombok.RequiredArgsConstructor;
import model.Model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Main controller .
 *
 * @see CustomersConductor
 * @see ElevatorsConductor
 */

@RequiredArgsConstructor
public class AppController implements ProtocolMessageListener {
    public final ElevatorsConductor elevatorsConductor = new ElevatorsConductor(this);
    private final CustomersConductor customerController = new CustomersConductor(this);
    public Model model;
    public final Gates gates = new Gates(new Server(), this);

    private final int TPS = 50;
    private double gameSpeed = 1;
    private long currentTime;


    public void start() {
        currentTime = System.currentTimeMillis();
        gates.setOnConnectEvent(
                () -> gates.send(
                        Protocol.APPLICATION_SETTINGS,
                        new InitializationSettingsForClient(
                                elevatorsConductor.SETTINGS,
                                customerController.CUSTOMERS_SETTINGS,
                                gameSpeed)));
        gates.setSpamEvent(
                () -> gates.send(
                        Protocol.UPDATE_DATA, model.getDataToSent()),
                (long) (1000. / ConnectionSettings.SSPS));

        gates.start();

        while (true) {
            long deltaTime = System.currentTimeMillis() - currentTime;
            currentTime += deltaTime;

            tickControllers((long) (deltaTime * gameSpeed));
            gates.tick(deltaTime);

            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void tickControllers(long deltaTime) {
        customerController.tick(deltaTime);
        elevatorsConductor.tick(deltaTime);
        model.clearDead();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean popMessage(ProtocolMessage message) {
        Protocol protocol = message.getProtocolInMessage();
        Serializable data = message.getDataInMessage();
        switch (protocol) {
            case CREATE_CUSTOMER -> {
                LinkedList<Integer> floors = (LinkedList<Integer>) data;
                customerController.CreateCustomer(floors.get(1), floors.get(0));
            }
            case CHANGE_ELEVATORS_COUNT -> {
                elevatorsConductor.changeElevatorsCount((boolean) data);
                gates.send(Protocol.APPLICATION_SETTINGS, new InitializationSettingsForClient(
                        elevatorsConductor.SETTINGS, customerController.CUSTOMERS_SETTINGS, gameSpeed));
            }
            case CHANGE_GAME_SPEED -> {
                gameSpeed *= (double) data;
                gates.send(Protocol.CHANGE_GAME_SPEED, gameSpeed);
            }
        }
        return true;
    }

    public void Send(Protocol protocol, Serializable data) {
        gates.send(protocol, data);
    }

    public void setModel(Model model) {
        this.model = model;
        customerController.setModel(model);
        elevatorsConductor.setModel(model);
    }
}
