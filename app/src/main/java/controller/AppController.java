package controller;

import configs.ConnectionSettings;
import controller.subControllers.*;
import dualConnectionStation.Server;
import gates.Gates;
import gates.SendFilters;
import protocol.*;
import protocol.special.SubscribeRequest;
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
public class AppController extends ControllerEndlessLoop implements MessageApplier {
    private final Gates gates = new Gates(new Server(), this);
    private final AppModel appModel = new AppModel();

    public void start() {
        gates.setOnConnectEvent(() -> gates.sendWithoutCheck(Protocol.HELLO_MESSAGE, -1, null));
        gates.setSpamEvent(this::spamWithData, (long) (1000. / ConnectionSettings.SSPS));
        gates.connect();

        addTickable(gates);
        addTickable(appModel);
        super.start();
    }

    private void spamWithData() {
        appModel.getGameMaps().forEach(
                appModel -> {
                    try {
                        gates.send(
                                Protocol.UPDATE_DATA,
                                appModel.getRoomId(),
                                appModel.createCompactGameMapData()
                        );
                    } catch (Gates.NobodyReceivedMessageException e) {
                        appModel.setDead(true);
                    }
                }
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean applyMessage(ProtocolMessage message) {
        Protocol protocol = message.getProtocol();
        Serializable data = message.getData();
        switch (protocol) {
            case SUBSCRIBE_FOR -> {
                gates.setSendFilter(message.getOwner(), SendFilters.sendOnlyIfSubscribed((SubscribeRequest) data));
                var subscribes = ((SubscribeRequest) data).roomsToSubscribeFor();
                appModel.createIfNotExist(subscribes, gates);
                gates.sendWithoutCheckMultiple(Protocol.ROOMS_PREPARE_SETTINGS, appModel.createRoomPrepareCompactData(subscribes));
            }
            case CREATE_CUSTOMER -> {
                LinkedList<Integer> floorsStartEnd = (LinkedList<Integer>) data;
//                appModel.getMap(message.getWorldId()).CreateCustomer(floorsStartEnd.get(1), floorsStartEnd.get(0));
            }
            case CHANGE_ELEVATORS_COUNT -> {
                //              appModel.getMap(message.getWorldId()).changeElevatorsCount((boolean) data);
            }
            case CHANGE_GAME_SPEED -> {
                multiplyControllerSpeedBy((double) data);
//                gates.send(Protocol.CHANGE_GAME_SPEED, this.getControllerTimeSpeed());
            }
        }
        return true;
    }

    @Override
    public int getTickPerSecond() {
        return AppControllerConfig.TPS;
    }
}
