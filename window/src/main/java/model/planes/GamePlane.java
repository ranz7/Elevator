package model.planes;

import drawable.abstracts.Drawable;
import drawable.concretes.FlyingText;
import drawable.concretes.game.elevator.DrawableElevator;
import lombok.Getter;
import model.GameMap;
import settings.CombienedDrawSettings;
import tools.Vector2D;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;


public class GamePlane extends Plane {
    @Getter
    private final GameMap gameMap;

    public GamePlane(CombienedDrawSettings combienedDrawSettings) {
        super(combienedDrawSettings);
        this.gameMap = new GameMap(combienedDrawSettings);
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
    protected Drawable getDrawable() {
        return gameMap;
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
        gameMap.tick(deltaTime * getCombienedSettings().gameSpeed());
    }

    @Override
    public int getId() {
        return gameMap.getId();
    }

    public CombienedDrawSettings getCombienedSettings() {
        return (CombienedDrawSettings) settings;
    }

    @Override
    public void resize(Dimension size) {
        getScaler().updateSizes(size, gameMap.getBuildingSize());
    }
}
