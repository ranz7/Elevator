package model.planes;

import drawable.abstracts.Drawable;
import drawable.concretes.FlyingText;
import drawable.concretes.menu.MenuDrawable;
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
        super(localDrawSettings, new Painter(
                new Scaler(
                        new Vector2D(localDrawSettings.customerWidth() * 1.5,
                                localDrawSettings.customerWidth() * 1.5), 1.),
                gameMap.getSettings()));
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
