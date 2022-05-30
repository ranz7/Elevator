package controller;

import configs.RoomPrepareCompactData;
import configs.ConnectionSettings;
import dualConnectionStation.Client;
import gates.ReciveFilters;
import gates.Gates;
import gates.ScenarioBuilder;
import protocol.special.GameMapCompactData;
import protocol.Protocol;
import protocol.ProtocolMessage;
import protocol.ProtocolMessagesController;
import settings.configs.GuiControllerConfig;
import model.*;
import tools.Vector2D;
import view.gui.Gui;
import model.planes.Plane;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

/**
 * control window, created with Swing
 */
public class GuiController extends ControllerEndlessLoop implements ProtocolMessagesController {
    private final GuiModel windowModel = new GuiModel();
    private final Gui gui = new Gui(this, windowModel.getLocalDrawSetting());
    public final Gates gates = new Gates(new Client(), this);

    public void start() {
        ScenarioBuilder connectedScenario = new ScenarioBuilder()
                .add(ReciveFilters.catchOnlyHello())
                .add(ReciveFilters.catchOnlySettings())
                .add(ReciveFilters.catchOnlyUpdate());
        gates.setOnGatesCloseEvent(() -> {
            gates.setReceiveScenario(connectedScenario.build(ReciveFilters.noFilter()));
            gates.connect();
        });
        gates.setOnConnectEvent(() -> {
            gates.sendWithoutCheck(Protocol.SUBSCRIBE_FOR, -1, windowModel.getPlanesToSubscribeFor());
        });
        gates.setReceiveScenario(connectedScenario.build(ReciveFilters.noFilter()));
        gates.connect();

        gui.start();
        addTickable(gates);
        addTickable(gui);
        addTickable(windowModel);
        super.start();
    }

    @Override
    public boolean applyMessage(ProtocolMessage message) {
        Protocol protocol = message.getProtocol();
        Serializable data = message.getData();
        int roomId = message.getRoomId();
        switch (protocol) {
            case HELLO_MESSAGE -> {
                Logger.getAnonymousLogger().info("Controller says Hello!!");

            }
            case WORLDS_PREPARE_SETTINGS -> {
                RoomPrepareCompactData settings = (RoomPrepareCompactData) data;
                if (ConnectionSettings.VERSION != settings.version()) {
                    Logger.getLogger(GuiController.class.getName()).warning(("You have different versions with sever." +
                            " Your version: %s, server version %s%n")
                            .formatted(ConnectionSettings.VERSION, settings.version()));
                    return true;
                }
                settings.roomData().forEach(windowModel::updateRemoteSettings);
                setCurrentTime(message.getTimeStump());
                gui.resize();
            }

            case UPDATE_DATA -> {
                windowModel.updateMap(roomId, (GameMapCompactData) data);
            }
            case ELEVATOR_OPEN -> windowModel.getMap(roomId).getElevator((int) data).changeDoorsState(false);
            case ELEVATOR_CLOSE -> windowModel.getMap(roomId).getElevator((int) data).changeDoorsState(true);
            case CUSTOMER_GET_IN_OUT -> windowModel.getMap(roomId).getCustomer((int) data).changeBehindElevator();
            case ELEVATOR_BUTTON_CLICK -> windowModel.getGamePlane(roomId).leftMouseClicked((Vector2D) data);
        }
        return true;
    }
//
//    public void addCustomer(int startFloorButtonId, int endFloorId) {
//        LinkedList<Integer> data = new LinkedList<>();
//        data.push(startFloorButtonId);
//        data.push(endFloorId);
//        // TODO ID ID
//        gates.send(new ProtocolMessage(Protocol.CREATE_CUSTOMER, data));
//    }
//
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

    public List<Plane> getAllPlanes() {
        return windowModel.getPlanes();
    }
}
