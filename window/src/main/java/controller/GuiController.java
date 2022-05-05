package controller;

import configs.ConnectionSettings;
import configs.MainInitializationSettings;
import connector.Client;
import connector.Gates;
import connector.filtersAndScenarios.FilterScenarios;
import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
import connector.protocol.ProtocolMessageListener;
import model.GuiModel;
import model.objects.movingObject.CreaturesData;
import tools.tools.Vector2D;
import view.gui.Gui;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/*
 * control window, created with Swing
 * @see SwingWindow
 */
public class GuiController implements ProtocolMessageListener {
    static private final int TPS = 120;

    public final Gates gates = new Gates(new Client(), this);

    private final Gui gui = new Gui(this);
    private GuiModel windowModel;

    private long currentTime;
    private double gameSpeed = 1;

    public void setModel(GuiModel model) {
        windowModel = model;
        gui.setModel(model);
    }

    public void start() {
        long lastTime = System.currentTimeMillis();
        gates.setOnDisconnectEvent(() -> {
            gates.setScenario(FilterScenarios.catchSettingsThenUpdateThenAnything);
            gates.start();
            windowModel.clear();
        });
        gates.setScenario(FilterScenarios.catchSettingsThenUpdateThenAnything);
        gates.start();

        while (true) {
            long deltaTime = System.currentTimeMillis() - lastTime;
            lastTime += deltaTime;
            currentTime += deltaTime;
            gates.tick(deltaTime);
            if (!windowModel.isNeedToInitialise()) {
                gui.start();
                windowModel.getDrawableOjects().forEach(object -> object.tick((long) (deltaTime * gameSpeed)));
                windowModel.clearDead();
                gui.update();
            }

            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean popMessage(ProtocolMessage message) {
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
                windowModel.setSettings(settings);
                gameSpeed = settings.GAME_SPEED;
                currentTime = message.getTimeStumpInMessage();
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
            case CHANGE_GAME_SPEED -> gameSpeed = (double) data;
        }
        return true;
    }

    public void clickedAddCustomerButtonWithNumber(int startFloorButtonNumber, int endFloorNumber) {
        startFloorButtonNumber = windowModel.getSettings().FLOORS_COUNT - startFloorButtonNumber - 1;
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
}
