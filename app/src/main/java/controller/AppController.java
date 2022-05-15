package controller;

import configs.*;
import connector.*;
import connector.dualConnectionStation.Server;
import connector.protocol.*;
import databases.configs.AppControllerConfig;
import lombok.RequiredArgsConstructor;
import model.*;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Main controller .
 *
 * @see CustomersConductor
 * @see ElevatorsConductor
 */
@RequiredArgsConstructor
public class AppController extends ControllerEndlessLoop implements ProtocolMessagesConductor {

    public final Gates gates = new Gates(new Server(), this);
    public AppModel appModel;
    public final ElevatorsConductor elevatorsConductor = new ElevatorsConductor(gates);
    private final CustomersConductor customerConductor = new CustomersConductor(gates);


    public void start() {
        gates.setOnConnectEvent(
                () -> gates.send(
                        Protocol.APPLICATION_SETTINGS,
                        appModel.createMainInitializationSettingsToSend(
                                elevatorsConductor.getSettings(),
                                customerConductor.getSettings(),
                                this.getControllerTimeSpeed())));
        gates.setSpamEvent(
                () -> gates.send(
                        Protocol.UPDATE_DATA, appModel.getDataToSent()),
                (long) (1000. / ConnectionSettings.SSPS));
        gates.start();

        addTickable(customerConductor);
        addTickable(elevatorsConductor);
        addTickable(gates);
        addModel(appModel);
        super.start();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean applyMessage(ProtocolMessage message) {
        Protocol protocol = message.getProtocolInMessage();
        Serializable data = message.getDataInMessage();
        switch (protocol) {
            case CREATE_CUSTOMER -> {
                LinkedList<Integer> floors = (LinkedList<Integer>) data;
                customerConductor.CreateCustomer(floors.get(1), floors.get(0));
            }
            case CHANGE_ELEVATORS_COUNT -> {
                elevatorsConductor.changeElevatorsCount((boolean) data);
                gates.send(Protocol.APPLICATION_SETTINGS, appModel.createMainInitializationSettingsToSend(
                        elevatorsConductor.getSettings(), customerConductor.getSettings(), this.getControllerTimeSpeed()));
            }
            case CHANGE_GAME_SPEED -> {
                multiplyControllerSpeedBy((double) data);
                gates.send(Protocol.CHANGE_GAME_SPEED, this.getControllerTimeSpeed());
            }
        }
        return true;
    }

    public void Send(Protocol protocol, Serializable data) {
        gates.send(protocol, data);
    }

    @Override
    public int getTickPerSecond() {
        return AppControllerConfig.TPS;
    }
}
