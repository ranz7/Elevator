package controller;

import configs.*;
import connector.*;
import connector.dualConnectionStation.Client;
import connector.filtersAndScenarios.Filters;
import connector.filtersAndScenarios.ScenarioBuilder;
import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
import connector.protocol.ProtocolMessagesController;
import settings.configs.GuiControllerConfig;
import model.*;
import model.objects.RemoteAccessedData;
import tools.Vector2D;
import view.gui.Gui;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * control window, created with Swing
 */
public class GuiController extends ControllerEndlessLoop implements ProtocolMessagesController {
    public final Gates gates = new Gates(new Client(), this);
    private final Gui gui = new Gui(this);

    private GuiModel windowModel;

    public void setModel(GuiModel model) {
        windowModel = model;
        gui.setModel(model);
    }

    public void start() {
        ScenarioBuilder connectedScenario = new ScenarioBuilder()
                .add(Filters.catchOnlySettings)
                .add(Filters.catchOnlyUpdate);
        gates.setOnDisconnectEvent(() -> {
            gates.setScenario(connectedScenario.build(Filters.noFilter));
            gates.start();
            windowModel.clearDead();
        });
        gates.setScenario(connectedScenario.build(Filters.noFilter));
        gates.start();

        gui.start();
        addTickable(gates);
        addTickable(gui);
        addModel(windowModel);
        super.start();
    }

    @Override
    public boolean applyMessage(ProtocolMessage message) {
        Protocol protocol = message.getProtocolInMessage();
        Serializable data = message.getDataInMessage();
        switch (protocol) {
            case APPLICATION_SETTINGS -> {
                ConnectionEstalblishConfig settings = (ConnectionEstalblishConfig) data;
                if (ConnectionSettings.VERSION != settings.version) {
                    Logger.getLogger(GuiController.class.getName()).warning(("You have different versions with sever." +
                            " Your version: %s, server version %s%n")
                            .formatted(ConnectionSettings.VERSION, settings.version));
                    return true;
                }
                windowModel.setRemoteConfig(settings);
                setControllerTimeSpeed(settings.gameSpeed);
                setCurrentTime(message.getTimeStumpInMessage());
                gui.resize();
            }
            case UPDATE_DATA -> windowModel.applyArivedData((RemoteAccessedData) data);
            case ELEVATOR_BUTTON_CLICK -> clickButton((Vector2D) data);
            case ELEVATOR_OPEN -> windowModel.getElevator((long) data).changeDoorsState(false);
            case ELEVATOR_CLOSE -> windowModel.getElevator((long) data).changeDoorsState(true);
            case CUSTOMER_GET_IN_OUT -> windowModel.getCustomer((long) data).changeBehindElevator();
            case CHANGE_GAME_SPEED -> setControllerTimeSpeed((double) data);
        }
        return true;
    }

    public void clickedAddCustomerButtonWithNumber(int startFloorButtonNumber, int endFloorNumber) {
        startFloorButtonNumber = windowModel.getCombienedDrawSettings().floorsCount() - startFloorButtonNumber - 1;
        LinkedList<Integer> data = new LinkedList<>();
        data.push(startFloorButtonNumber);
        data.push(endFloorNumber - 1);
        gates.send(new ProtocolMessage(Protocol.CREATE_CUSTOMER, data));
    }

    public void changeElevatorsCount(boolean isAdding) {
        gates.send(new ProtocolMessage(Protocol.CHANGE_ELEVATORS_COUNT, isAdding));
    }

    public void increaseSpeed() {
        gates.send(new ProtocolMessage(Protocol.CHANGE_GAME_SPEED, 1.5));
    }

    public void decreaseGameSpeed() {
        gates.send(new ProtocolMessage(Protocol.CHANGE_GAME_SPEED, 1 / 1.5));
    }

    public void clickButton(Vector2D point) {
        var pointInGame = gui.getGameWindow().getGameScaler().getFromRealToGameCoordinate(point, 0);
        var button = windowModel.getNearestButton(pointInGame);
        if (button == null) {
            return;
        }
        if (button.getPosition().distanceTo(pointInGame) < 20) {
            button.buttonClick();
        }
    }

    @Override
    protected int getTickPerSecond() {
        return GuiControllerConfig.TPS;
    }
}
