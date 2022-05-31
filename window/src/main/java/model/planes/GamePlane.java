package model.planes;

import configs.RoomPrepareCompactData;
import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableCreature;
import drawable.concretes.FlyingText;
import lombok.Getter;
import lombok.Setter;
import model.DatabaseOf;
import model.GameMap;
import model.planes.graphics.Painter;
import model.planes.graphics.Scaler;
import settings.RoomRemoteSettings;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

import java.awt.*;


public class GamePlane extends Plane {
    @Getter
    private final GameMap gameMap;
    @Getter
    @Setter
    private RoomRemoteSettings roomRemoteSettings;

    public GamePlane(RoomRemoteSettings roomRemoteSettings, LocalDrawSetting localDrawSettings) {
        super(localDrawSettings, new Painter(new Scaler(new Vector2D(50, 50), 0.2)));
        this.gameMap = new GameMap(localDrawSettings);
        this.roomRemoteSettings = roomRemoteSettings;
    }

    @Override
    public void rightMouseClicked(Vector2D point) {
        if (zoomedIn()) {
            var gamePosition = getScaler().getFromRealToGameCoordinate(point, 4);
            gameMap.add(FlyingText.FlyingTextUpSlow("ZoomIn", gamePosition));
            zoomIn(point, 1 / 4.);
        } else {
            zoomOut();
        }
    }


    @Override
    public void leftMouseClicked(Vector2D point) {
        var pointInGame = getScaler().getFromRealToGameCoordinate(point, 0);
        gameMap.add(FlyingText.FlyingTextUpFast("Click", pointInGame));
        var button = gameMap.getNearestButton(pointInGame);
        if (button == null) {
            return;
        }
        if (button.getPosition().distance(pointInGame) < 20) {
            button.buttonClick();
        }
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
        gameMap.tick(deltaTime * roomRemoteSettings.gameSpeed());
    }

    @Override
    protected DatabaseOf<Drawable> getLocalDataBase() {
        return gameMap.getLocalDataBase();
    }

    @Override
    public int getId() {
        return gameMap.getId();
    }

    @Override
    public void resize(Dimension size) {
        getScaler().updateSizes(size, gameMap.getBuildingSize());
    }

}
