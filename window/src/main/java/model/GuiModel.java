package model;

import configs.RoomPrepareCompactData;
import controller.GuiController;
import controller.Tickable;

import controller.TickableList;
import lombok.Getter;
import model.planes.MenuPlane;
import protocol.special.GameMapCompactData;
import protocol.special.SubscribeRequest;
import settings.RoomRemoteSettings;
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

    public GuiModel(GuiController controller) {
        planes.add(new MenuPlane(controller, localDrawSetting));
    }


    public void updateRemoteSettings(RoomPrepareCompactData.RoomData roomPrepareCompactData) {
        planes.stream().
                filter(plane -> plane instanceof GamePlane).
                filter(plane -> ((GamePlane) plane).getRoomRemoteSettings().roomId() == roomPrepareCompactData.roomId()).
                findFirst().
                ifPresentOrElse(
                        plane ->
                                ((GamePlane) plane).setRoomRemoteSettings(new RoomRemoteSettings(roomPrepareCompactData)),
                        () -> {
                            planes.add(new GamePlane(new RoomRemoteSettings(roomPrepareCompactData), localDrawSetting));
                            getMenuPlane().roomWasCreated(roomPrepareCompactData.roomId());
                        }
                );

    }

    @Override
    public void tick(double deltaTime) {
        new TickableList(planes).tick(deltaTime);
        System.out.println(getPlanesToSubscribeFor());
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
        streamOfGamePlanes().filter(gamePlane -> gamePlane.getRoomRemoteSettings().roomId() == roomId).
                findFirst().ifPresentOrElse(plane -> ref.found = plane, () -> {
                    throw new RuntimeException("Not found");
                });
        return ref.found;
    }

    public Plane getActivePlane() {
        return planes.stream().filter(Plane::isActive).findFirst().get();
    }

    public void updateMap(int mapId, GameMapCompactData data) {
        getMap(mapId).applyArrivedData(data);
    }

    public SubscribeRequest getPlanesToSubscribeFor() {
        var gamePlanesUsedInPortals = getMenuPlane().getUsedGamePlanesInPortals();
        return new SubscribeRequest(gamePlanesUsedInPortals.stream().distinct().collect(Collectors.toList()));
    }

    public void removeGamePlane(int roomId) {
        planes.removeIf(plane -> plane instanceof GamePlane && ((GamePlane) plane).getRoomRemoteSettings().roomId() == roomId);
        getMenuPlane().roomWasDeleted(roomId);
    }

    public MenuPlane getMenuPlane() {
        return (MenuPlane) planes.get(0);
    }
}
