package controller;

import dualConnectionStation.Client;
import gates.ReciveFilters;
import gates.Gates;
import gates.ScenarioBuilder;
import model.planes.MenuPlane;
import protocol.special.GameMapCompactData;
import protocol.Protocol;
import protocol.ProtocolMessage;
import protocol.MessageApplier;
import protocol.special.SubscribeRequest;
import settings.configs.GuiControllerConfig;
import model.*;
import view.gui.Gui;
import model.planes.Plane;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * control window, created with Swing
 */
public class GuiController extends ControllerEndlessLoop implements MessageApplier {
    private final GuiModel windowModel = new GuiModel(this);
    private final Gui gui = new Gui(this, windowModel.getLocalDrawSetting());
    public final Gates gates = new Gates(new Client(), this);

    public void start() {
        ScenarioBuilder connectedScenario =
                new ScenarioBuilder()
                .add(ReciveFilters.catchOnlyHello())
                .add(ReciveFilters.catchOnlyUpdate());


        windowModel.getMenuPlane().changeDoorsState(true);
        gates.setOnConnectEvent(() -> {
            windowModel.getMenuPlane().changeDoorsState(false);
            lastRoomsToSubscribeFor.clear();
        });

        gates.setOnGatesLostSocketEvent((ignored) -> {
            windowModel.getMenuPlane().changeDoorsState(true);
            gates.setReceiveScenario(connectedScenario.build(ReciveFilters.noFilter()));
            gates.connect();
        });

        gates.setReceiveScenario(connectedScenario.build(ReciveFilters.noFilter()));
        gates.connect();

        addTickable(gates);
        addTickable(gui);
        addTickable(windowModel);
        super.start(this::updateSubscribes);
    }


    @Override
    public boolean applyMessage(ProtocolMessage message) {
        Protocol protocol = message.getProtocol();
        Serializable data = message.getData();
        int roomId = message.getRoomId();
        switch (protocol) {
            case HELLO_MESSAGE -> {
                Logger.getAnonymousLogger().info("Controller says Hello!!");
                lastRoomsToSubscribeFor.clear();
                updateSubscribes();
            }
            case UPDATE_DATA -> {
                windowModel.updateMap(message.getRoomId(), (GameMapCompactData) data, gates);
                gui.updatePing();
            }
            case ELEVATOR_OPEN -> windowModel.getMap(roomId).ifPresent(map -> map.getElevator((int) data).changeDoorsState(false));
            case ELEVATOR_CLOSE -> windowModel.getMap(roomId).ifPresent(map -> map.getElevator((int) data).changeDoorsState(true));
            case CUSTOMER_GET_IN_OUT -> windowModel.getMap(roomId).ifPresent(map -> map.getCustomer((int) data).changeBehindElevator());
            case ELEVATOR_BUTTON_CLICK -> windowModel.getMap(roomId).ifPresent(gameMap -> gameMap.getButton((int) (data)).buttonClick());
        }
        return true;
    }

//    public void changeElevatorsCount(boolean isAdding) {
//        gates.send(new ProtocolMessage(Protocol.CHANGE_ELEVATORS_COUNT, isAdding));
//    }
//
//    public void increaseSpeed() {
//        gates.send(new ProtocolMessage(Protocol.CHANGE_GAME_SPEED, 1.5));
//    }
//
//    public void decreaseGameSpeed() {
//        gates.send(new ProtocolMessage(Protocol.CHANGE_GAME_SPEED, 1 / 1.5));
//    }

    @Override
    protected int getTickPerSecond() {
        return GuiControllerConfig.TPS;
    }

    public Plane getActivePlane() {
        return windowModel.getActivePlane();
    }

    private List<Integer> lastRoomsToSubscribeFor = new LinkedList<>();

    public void updateSubscribes() {
        var currentSubscribtions = windowModel.getPlanesToSubscribeFor().roomsToSubscribeFor();
        if (!lastRoomsToSubscribeFor.equals(currentSubscribtions)) {
            lastRoomsToSubscribeFor = currentSubscribtions;
            gates.sendWithoutCheck(Protocol.SUBSCRIBE_FOR, -1, new SubscribeRequest(currentSubscribtions));
        }
    }

    public MenuPlane getMenu() {
        return windowModel.getMenuPlane();
    }

    public void changeServer() {
         gates.changeServer();
    }
}
