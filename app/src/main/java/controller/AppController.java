package controller;

<<<<<<< Updated upstream
import configs.ConnectionSettings;
import connector.Gates;
import connector.Server;
import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
import connector.protocol.ProtocolMessageListener;
=======
import configs.*;
import connector.*;
import connector.dualConnectionStation.Server;
import connector.protocol.*;
import controller.subControllers.CustomersController;
import controller.subControllers.ElevatorsController;
import settings.configs.AppControllerConfig;
>>>>>>> Stashed changes
import lombok.RequiredArgsConstructor;
import model.AppModel;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Main controller .
 *
 * @see CustomersController
 * @see ElevatorsController
 */
<<<<<<< Updated upstream

@RequiredArgsConstructor
public class AppController implements ProtocolMessageListener {
    public final ElevatorsConductor elevatorsConductor = new ElevatorsConductor(this);
    private final CustomersConductor customerConductor = new CustomersConductor(this);
    public AppModel appModel;
    public final Gates gates = new Gates(new Server(), this);

    private final int TPS = 50;
    private double gameSpeed = 1;
    private long currentTime;
=======
@RequiredArgsConstructor
public class AppController extends ControllerEndlessLoop implements ProtocolMessagesController, ControllerConnector {
    private final AppModel appModel = new AppModel();
    private final ElevatorsController elevatorsController = new ElevatorsController(gates, this);
    private final CustomersController customerConductor = new CustomersController(gates, this);
    private final Gates gates = new Gates(new Server(), this);
>>>>>>> Stashed changes


    public void start() {
        currentTime = System.currentTimeMillis();
        gates.setOnConnectEvent(
                () -> gates.send(
                        Protocol.APPLICATION_SETTINGS,
<<<<<<< Updated upstream
                        appModel.createMainInitializationSettingsToSend(
                                elevatorsConductor.getSettings(),
                                customerConductor.getSettings(),
                                gameSpeed)));
=======
                        appModel.createMainInitializationSettingsToSend(this.getControllerTimeSpeed())));
>>>>>>> Stashed changes
        gates.setSpamEvent(
                () -> gates.send(
                        Protocol.UPDATE_DATA, appModel.sendMap()),
                (long) (1000. / ConnectionSettings.SSPS));

        gates.start();

<<<<<<< Updated upstream
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
        customerConductor.tick(deltaTime);
        elevatorsConductor.tick(deltaTime);
        appModel.clearDead();
=======
        addTickable(customerConductor);
        addTickable(elevatorsController);
        addTickable(gates);
        addModel(appModel);
        super.start();
>>>>>>> Stashed changes
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean popMessage(ProtocolMessage message) {
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
<<<<<<< Updated upstream
                        elevatorsConductor.getSettings(), customerConductor.getSettings(), gameSpeed));
=======
                        elevatorsController.getSettings(), customerConductor.getSettings(), this.getControllerTimeSpeed()));
>>>>>>> Stashed changes
            }
            case CHANGE_GAME_SPEED -> {
                gameSpeed *= (double) data;
                gates.send(Protocol.CHANGE_GAME_SPEED, gameSpeed);
            }
        }
        return true;
    }

<<<<<<< Updated upstream
    public void Send(Protocol protocol, Serializable data) {
        gates.send(protocol, data);
    }

    public void setAppModel(AppModel appModel) {
        this.appModel = appModel;
        customerConductor.setModel(appModel);
        elevatorsConductor.setModel(appModel);
=======
    @Override
    public int getTickPerSecond() {
        return AppControllerConfig.TPS;
>>>>>>> Stashed changes
    }
}
