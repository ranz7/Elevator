package controller;

import configs.*;
import connector.*;
import connector.protocol.*;
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
public class AppController extends ControllerEndlessLoop implements ProtocolMessageListener {
    public final ElevatorsConductor elevatorsConductor = new ElevatorsConductor(this);
    private final CustomersConductor customerConductor = new CustomersConductor(this);

    public final Gates gates = new Gates(new Server(), this);

    public AppModel appModel;

    public void setAppModel(AppModel appModel) {
        this.appModel = appModel;
        customerConductor.setModel(appModel);
        elevatorsConductor.setModel(appModel);
    }

    public void start() {
        gates.setOnConnectEvent(
                () -> gates.send(
                        Protocol.APPLICATION_SETTINGS,
                        appModel.createMainInitializationSettingsToSend(
                                elevatorsConductor.getSettings(),
                                customerConductor.getSettings(),
                                getControllerSpeed())));
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
                        elevatorsConductor.getSettings(), customerConductor.getSettings(), getControllerSpeed()));
            }
            case CHANGE_GAME_SPEED -> {
                multiplyControllerSpeedBy((double) data);
                gates.send(Protocol.CHANGE_GAME_SPEED, getControllerSpeed());
            }
        }
        return true;
    }

    public void Send(Protocol protocol, Serializable data) {
        gates.send(protocol, data);
    }

    @Override
    int getTickPerSecond() {
        return MainControllerSettings.TPS;
    }
}
