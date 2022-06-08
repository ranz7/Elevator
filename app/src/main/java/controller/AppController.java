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
import tools.Pair;

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
                                appModel.createCompactGameMapData());
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
            }
            case CREATE_CUSTOMER -> {
                Pair<Integer,Boolean> floorIdIfLeft= (Pair<Integer, Boolean>) data;
                 appModel.getMap(message.getRoomId()).createCustomer(floorIdIfLeft.getFirst(),
                         floorIdIfLeft.getSecond());
            }
            case CHANGE_ELEVATORS_COUNT -> {
                //              appModel.getMap(message.getWorldId()).changeElevatorsCount((boolean) data);
            }
            case CHANGE_GAME_SPEED -> {
                appModel.getMap(message.getRoomId()).changeGameSpeed((double)data);
            }
        }
        return true;
    }

    @Override
    public int getTickPerSecond() {
        return AppControllerConfig.TPS;
    }
}
