package model;

import configs.RoomPrepareCompactData;
import configs.ConnectionSettings;
import controller.Tickable;
import controller.TickableList;
import gates.Gates;
import model.objects.Creature;
import model.objects.GameMap;
import protocol.special.GameMapCompactData;
import settings.LocalCreaturesSettings;
import lombok.Getter;
import tools.Pair;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/*
 * Class to store all objects.
 */

public class AppModel implements Tickable {
    @Getter
    private final LocalCreaturesSettings localCreaturesSettings = new LocalCreaturesSettings();

    @Getter
    LinkedList<GameMap> gameMaps = new LinkedList<>();

    @Override
    public void tick(double deltaTime) {
        new TickableList(gameMaps).tick(deltaTime);
        gameMaps.removeIf(Creature::isDead);
    }

    public List<Pair<Integer, Serializable>> createRoomPrepareCompactData(List<Integer> subscribes) {
        List<Pair<Integer, Serializable>> roomsData = new LinkedList<>();
        subscribes.forEach(roomId -> roomsData.add(new Pair<>(
                roomId,
                new RoomPrepareCompactData(ConnectionSettings.VERSION, getMap(roomId).toRoomData()))));
        return roomsData;
    }


    public GameMap getMap(long roomId) {
        return gameMaps.stream().filter(gameMap -> gameMap.getRoomId() == roomId).findFirst().get();
    }

    public void createIfNotExist(List<Integer> roomsToSubscribeFor, Gates controllerGates) {
        roomsToSubscribeFor.forEach(
                roomId -> {
                    var map = gameMaps.stream().filter(gameMap -> gameMap.getRoomId() == roomId).findFirst();
                    if (map.isEmpty()) {
                        gameMaps.add(new GameMap(controllerGates, roomId, localCreaturesSettings));
                    }
                }
        );
    }

}
