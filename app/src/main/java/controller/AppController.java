package controller;

import configs.*;
import connector.*;
import connector.dualConnectionStation.Server;
import connector.protocol.*;
import controller.subControllers.CustomersController;
import controller.subControllers.ElevatorsController;
import settings.configs.AppControllerConfig;
import lombok.RequiredArgsConstructor;
import model.*;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Main controller .
 *
 * @see CustomersController
 * @see ElevatorsController
 */
@RequiredArgsConstructor
public class AppController extends ControllerEndlessLoop implements ProtocolMessagesController, ControllerConnector {
    private final AppModel appModel = new AppModel();
    private final ElevatorsController elevatorsController = new ElevatorsController(gates, this);
    private final CustomersController customerConductor = new CustomersController(gates, this);
    private final Gates gates = new Gates(new Server(), this);


    public void start() {
        gates.setOnConnectEvent(
                () -> gates.send(
                        Protocol.APPLICATION_SETTINGS,
                        appModel.createMainInitializationSettingsToSend(this.getControllerTimeSpeed())));
        gates.setSpamEvent(
                () -> gates.send(
                        Protocol.UPDATE_DATA, appModel.sendMap()),
                (long) (1000. / ConnectionSettings.SSPS));
        gates.start();

        addTickable(customerConductor);
        addTickable(elevatorsController);
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
                elevatorsController.changeElevatorsCount((boolean) data);
                gates.send(Protocol.APPLICATION_SETTINGS, appModel.createMainInitializationSettingsToSend(
                        elevatorsController.getSettings(), customerConductor.getSettings(), this.getControllerTimeSpeed()));
            }
            case CHANGE_GAME_SPEED -> {
                multiplyControllerSpeedBy((double) data);
                gates.send(Protocol.CHANGE_GAME_SPEED, this.getControllerTimeSpeed());
            }
        }
        return true;
    }

    @Override
    public int getTickPerSecond() {
        return AppControllerConfig.TPS;
    }
}
