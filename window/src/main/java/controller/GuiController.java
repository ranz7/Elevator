package controller;

import configs.*;
import connector.*;
import connector.filtersAndScenarios.FilterScenarios;
import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
import connector.protocol.ProtocolMessageListener;
import model.*;
import model.objects.movingObject.*;
import tools.Vector2D;
import view.gui.Gui;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * control window, created with Swing
 */
public class GuiController extends ControllerEndlessLoop implements ProtocolMessageListener {
    public final Gates gates = new Gates(new Client(), this);
    private final Gui gui = new Gui(this);

    private GuiModel windowModel;

    public void setModel(GuiModel model) {
        windowModel = model;
        gui.setModel(model);
    }

    public void start() {
        gates.setOnDisconnectEvent(() -> {
            gates.setScenario(FilterScenarios.catchSettingsThenUpdateThenAnything);
            gates.start();
            windowModel.clear();
        });
        gates.setScenario(FilterScenarios.catchSettingsThenUpdateThenAnything);
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
                MainInitializationSettings settings = (MainInitializationSettings) data;
                if (ConnectionSettings.VERSION != settings.VERSION) {
                    Logger.getLogger(GuiController.class.getName())
                            .warning("You have different versions with sever. Your version: %s, server version %s%n"
                                    .formatted(ConnectionSettings.VERSION, settings.VERSION));
                    return true;
                }
                windowModel.setMainInitializationSettings(settings);
                setControllerSpeed(settings.GAME_SPEED);
                setCurrentTime(message.getTimeStumpInMessage());
                gui.resize();
            }
            case UPDATE_DATA -> {
                windowModel.updateData((CreaturesData) data);
            }
            case ELEVATOR_BUTTON_CLICK -> {
                clickButton((Vector2D) data);
            }
            case ELEVATOR_OPEN -> windowModel.getElevator((long) data).DOORS.changeDoorsState(false);
            case ELEVATOR_CLOSE -> windowModel.getElevator((long) data).DOORS.changeDoorsState(true);
            case CUSTOMER_GET_IN_OUT -> windowModel.changeBehindElevatorForCustomer((long) data);
            case CHANGE_GAME_SPEED -> setControllerSpeed((double) data);
        }
        return true;
    }

    public void clickedAddCustomerButtonWithNumber(int startFloorButtonNumber, int endFloorNumber) {
        startFloorButtonNumber = windowModel.getMainInitializationSettings().FLOORS_COUNT - startFloorButtonNumber - 1;
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
        if (button.getPosition().distanceTo(pointInGame) < 20) {
            button.buttonClick();
        }
    }

    @Override
    int getTickPerSecond() {
        return 120;
    }
}
