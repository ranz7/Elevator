package model;

import configs.RoomPrepareCompactData;
import controller.Tickable;

import controller.TickableList;
import lombok.Getter;
import model.planes.MenuPlane;
import protocol.special.GameMapCompactData;
import protocol.special.SubscribeRequest;
import settings.CombienedDrawSettings;
import settings.localDraw.LocalDrawSetting;
import model.planes.GamePlane;
import model.planes.Plane;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GuiModel implements Tickable {
    @Getter
    private final List<Plane> planes = new LinkedList<>();
    @Getter
    private final LocalDrawSetting localDrawSetting = new LocalDrawSetting();

    public GuiModel() {
        planes.add(new MenuPlane(localDrawSetting));
    }

    public void updateRemoteSettings(RoomPrepareCompactData.RoomData roomPrepareCompactData) {
        planes.stream().
                filter(plane -> plane instanceof GamePlane).
                filter(plane -> ((GamePlane) plane).getCombienedSettings().roomId() == roomPrepareCompactData.roomId()).
                findFirst().
                ifPresentOrElse(
                        plane -> ((GamePlane) plane).getCombienedSettings().setRoomPrepareCompactData(roomPrepareCompactData),
                        () -> planes.add(new GamePlane(new CombienedDrawSettings(roomPrepareCompactData)))
                );
    }

    @Override
    public void tick(double deltaTime) {
        new TickableList(planes).tick(deltaTime);
    }

    public GameMap getMap(int roomId) {
        var plane = getGamePlane(roomId);
        return plane.getGameMap();
    }

    public Stream<GamePlane> streamOfGamePlanes() {
        return planes.stream().
                filter(plane -> plane instanceof GamePlane).map(plane -> (GamePlane) plane);
    }

    public GamePlane getGamePlane(int roomId) {
        var ref = new Object() {
            GamePlane found;
        };
        streamOfGamePlanes().filter(gamePlane -> gamePlane.getCombienedSettings().roomId() == roomId).
                findFirst().ifPresentOrElse(plane -> ref.found = (GamePlane) plane, () -> {
                    throw new RuntimeException("Not found");
                });
        return ref.found;
    }

    public Runnable createWorld(GameMapCompactData data) {
        return null;
    }

    public Plane getActivePlane() {
        return planes.stream().filter(Plane::isActive).findFirst().get();
    }

    public void updateMap(int mapId, GameMapCompactData data) {

    }

    public SubscribeRequest getPlanesToSubscribeFor() {
        return new SubscribeRequest(
                streamOfGamePlanes()
                        .map(gamePlane ->   gamePlane.getCombienedSettings().roomId())
                        .collect(Collectors.toList())
        );

    }
}
