package model.planes;

import drawable.abstracts.Drawable;
import drawable.concretes.FlyingText;
import lombok.Getter;
import model.DatabaseOf;
import model.GameMap;
import model.planes.graphics.Painter;
import model.planes.graphics.Scaler;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;



public class GamePlane extends Plane {
    @Getter
    private final GameMap gameMap;

    public GamePlane(LocalDrawSetting localDrawSettings, GameMap gameMap) {
        super(localDrawSettings, new Painter(new Scaler(new Vector2D(localDrawSettings.customerWidth() , localDrawSettings.customerWidth()), 0.2)));
        this.gameMap = gameMap;
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
        updateDrawingObjectsForThreadSafety();
    }

    @Override
    protected DatabaseOf<Drawable> getLocalDataBase() {
        return gameMap.getLocalDataBase();
    }

    @Override
    public void resize(Vector2D size) {
        getScaler().updateSizes(size, gameMap.getSize());
    }

}
