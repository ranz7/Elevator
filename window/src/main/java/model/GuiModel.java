package model;

import configs.ConnectionSettings;
import controller.GuiController;
import controller.Tickable;

import controller.TickableList;
import drawable.concretes.menu.Portal;
import gates.Gates;
import lombok.Getter;
import model.objects.Creature;
import model.packageLoader.DrawableCreatureData;
import model.packageLoader.PackageLoader;
import model.planes.MenuPlane;
import protocol.special.GameMapCompactData;
import protocol.special.SubscribeRequest;
import settings.RoomRemoteSettings;
import settings.localDraw.LocalDrawSetting;
import model.planes.Plane;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GuiModel implements Tickable {
    @Getter
    private MenuPlane menuPlane;
    private List<GameMap> gameMaps = new LinkedList<>();

    @Getter
    private final LocalDrawSetting localDrawSetting = new LocalDrawSetting();

    public GuiModel(GuiController controller) {
        menuPlane = new MenuPlane(controller, localDrawSetting);
    }


    @Override
    public void tick(double deltaTime) {
        gameMaps.removeIf(Creature::isDead);
        menuPlane.tick(deltaTime);
        new TickableList(gameMaps).tick(deltaTime);
        updatePortals();
    }


    private void updatePortals() {
        gameMaps.forEach(map->map.setDead(true));
        // give map for portal
        streamOfGameMaps().forEach(
                gameMap -> getMenuPlane().streamOfPortals().forEach(
                        portal -> {
                            if (portal.getRoomId() == gameMap.getRoomRemoteSettings().roomId()) {
                                portal.setGameMap(gameMap);
                                gameMap.setDead(false);
                            }
                            if (portal.getRoomId() == -1) {
                                portal.setGameMap(null);
                            }
                        }
                )
        );
    }

    public Optional<GameMap> getMap(int roomId) {
        return streamOfGameMaps()
                .filter(gameMap -> gameMap.getRoomRemoteSettings().roomId() == roomId)
                .findFirst();
    }

    public Stream<GameMap> streamOfGameMaps() {
        return gameMaps.stream();
    }

    public Plane getActivePlane() {
        if (menuPlane.isActive()) {
            return menuPlane;
        }
        return menuPlane.streamOfPortals().map(Portal::getPlane).filter(Objects::nonNull)
                .filter(Plane::isActive).findFirst().get();
    }

    public void updateMap(int roomId , GameMapCompactData data, Gates gates) {
        if (ConnectionSettings.VERSION != data.roomData.version()) {
            Logger.getLogger(GuiController.class.getName()).warning(("You have different versions with sever." +
                    " Your version: %s, server version %s%n")
                    .formatted(ConnectionSettings.VERSION, data.roomData.version()));
        }

        Optional<GameMap> mapToUpdate = getMap(roomId );
        if (mapToUpdate.isEmpty()) {
            mapToUpdate = Optional.of(new GameMap(
                    new DrawableCreatureData(data.parentIdClassTypeObject.get(0)),
                    localDrawSetting,
                    new RoomRemoteSettings(data.roomData),
                    gates
            ));
            gameMaps.add(mapToUpdate.get());
        }
        mapToUpdate.get().setRoomRemoteSettings(new RoomRemoteSettings(data.roomData));
        PackageLoader.applyArrivedData(data, mapToUpdate.get());

    }

    public SubscribeRequest getPlanesToSubscribeFor() {
        var gamePlanesUsedInPortals = getMenuPlane().getUsedGamePlanesInPortals();
        return new SubscribeRequest(gamePlanesUsedInPortals.stream().distinct().collect(Collectors.toList()));
    }

    public void clear() {
        menuPlane.streamOfPortals().forEach(Portal::close);
        gameMaps.clear();
    }
}
