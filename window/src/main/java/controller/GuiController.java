package controller;

import connector.clientServer.Client;
import connector.clientServer.ConnectionSettings;
import connector.clientServer.SocketCompactData;
import connector.clientServer.SocketEventListener;
import connector.protocol.CreaturesData;
import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
import connector.protocol.SettingsData;
import model.GuiModel;
import common.Vector2D;
import view.gui.Gui;
import lombok.Setter;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/*
 * control window, created with Swing
 * @see SwingWindow
 */
public class GuiController implements SocketEventListener {
    static private final int TPS = 50;

    private final GuiModel WINDOW_MODEL;
    private final LinkedList<ProtocolMessage> MESSAGE = new LinkedList<>();
    private final Gui GUI;

    @Setter
    private long currentTime;
    private double gameSpeed = 1;
    @Setter
    private Client client;

    private final Logger LOGGER = Logger.getLogger(GuiController.class.getName());

    public GuiController(GuiModel guiModel) {
        WINDOW_MODEL = guiModel;
        GUI = new Gui(WINDOW_MODEL,this);
    }

    public void start() throws InterruptedException {
        GUI.start();
        long lastTime = System.currentTimeMillis();

        while (true) {
            clientConnectReadWrite();
            long deltaTime = System.currentTimeMillis() - lastTime;
            lastTime += deltaTime;
            currentTime += deltaTime;

            if (!WINDOW_MODEL.isNeedToInitialise()) {
                WINDOW_MODEL.getDrawableOjects().forEach(object -> object.tick((long) (deltaTime * gameSpeed)));
                WINDOW_MODEL.clearDead();
            }

            GUI.update();
            TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
            WINDOW_MODEL.clearDead();
        }
    }

    @Override
    public void onReceiveSocket(ProtocolMessage message) {
        if (WINDOW_MODEL.getSettings() == null && message.protocol() != Protocol.APPLICATION_SETTINGS) {
            return;
        }
        if (WINDOW_MODEL.isNeedToInitialise() && message.protocol() != Protocol.APPLICATION_SETTINGS
                && message.protocol() != Protocol.UPDATE_DATA) {
            return;
        }
        synchronized (MESSAGE) {
            MESSAGE.add(message);
        }
    }

    @Override
    public void onNewSocketConnection(SocketCompactData message) {
        LOGGER.info("Connected");
    }


    private void clientConnectReadWrite() {
        if (client.isClosed()) {
            WINDOW_MODEL.clear();
            client.reconnect();
        }
        synchronized (MESSAGE) {
            MESSAGE.removeIf(this::processMessage);
        }
    }

    private boolean processMessage(ProtocolMessage message) {
        if (message.protocol() != Protocol.APPLICATION_SETTINGS && message.protocol() != Protocol.UPDATE_DATA) {
            //  if (message.timeStump() + 2 * WINDOW_MODEL.getLastServerRespondTime() > currentTime) {
            //      return false;
            //  }
        }
        switch (message.protocol()) {
            case APPLICATION_SETTINGS -> {
                SettingsData settings = (SettingsData) message.data();
                if (ConnectionSettings.VERSION != settings.VERSION) {
                    LOGGER.warning("You have different versions with sever. Your version: %s, server version %s%n"
                            .formatted(ConnectionSettings.VERSION, settings.VERSION));
                    return true;
                }
                WINDOW_MODEL.setSettings(settings);
                gameSpeed = settings.GAME_SPEED;
                currentTime = message.timeStump();
            }
            case UPDATE_DATA -> {
                WINDOW_MODEL.updateData((CreaturesData) message.data());
            }
            case ELEVATOR_BUTTON_CLICK -> {
                clickButton((Vector2D) message.data());
            }
            case ELEVATOR_OPEN -> WINDOW_MODEL.getElevator((long) message.data()).DOORS.changeDoorsState(false);
            case ELEVATOR_CLOSE -> WINDOW_MODEL.getElevator((long) message.data()).DOORS.changeDoorsState(true);
            case CUSTOMER_GET_IN_OUT -> WINDOW_MODEL.changeBehindElevatorForCustomer((long) message.data());
            case CHANGE_GAME_SPEED -> gameSpeed = (double) message.data();
        }
        return true;
    }

    public void clickedAddCustomerButtonWithNumber(int startFloorButtonNumber, int endFloorNumber) {
        startFloorButtonNumber = WINDOW_MODEL.getSettings().FLOORS_COUNT - startFloorButtonNumber - 1;
        LinkedList<Integer> data = new LinkedList<>();
        data.push(startFloorButtonNumber);
        data.push(endFloorNumber - 1);
        client.send(new ProtocolMessage(Protocol.CREATE_CUSTOMER, data, currentTime));
    }

    public void changeElevatorsCount(boolean isAdding) {
        client.send(new ProtocolMessage(Protocol.CHANGE_ELEVATORS_COUNT, isAdding, currentTime));
    }

    public void increaseSpeed() {
        client.send(new ProtocolMessage(Protocol.CHANGE_GAME_SPEED, 1.5, currentTime));
    }

    public void decreaseGameSpeed() {
        client.send(new ProtocolMessage(Protocol.CHANGE_GAME_SPEED, 1 / 1.5, currentTime));
    }

    public void clickButton(Vector2D point) {
        var pointInGame = GUI.getGameWindow().getGAME_SCALER().getFromRealToGameCoordinate(point, 0);
        var button = WINDOW_MODEL.getNearestButton(pointInGame);
        if (button.getPosition().distanceTo(pointInGame) <20) {
            button.buttonClick();

        }
    }
}
