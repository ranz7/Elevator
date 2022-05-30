package model.planes;

import drawable.abstracts.Drawable;
import drawable.concretes.menu.MenuDrawable;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

import java.awt.*;

public class MenuPlane extends Plane {
    private final MenuDrawable menuDrawable = new MenuDrawable(getSettings());

    public MenuPlane(LocalDrawSetting settings) {
        super(settings);
    }

    @Override
    public void rightMouseClicked(Vector2D point) {
//        if (zoomedIn()) {
//            var gamePosition = getScaler().getFromRealToGameCoordinate(point, 4);
//            gameMap.add(FlyingText.FlyingTextUpSlow("ZoomIn", gamePosition));
//            zoomIn(point, 1 / 4.);
//        } else {
//            zoomOut();
//        }
    }

    @Override
    public void leftMouseClicked(Vector2D point) {
    }

    @Override
    protected Drawable getDrawable() {
        return menuDrawable;
    }

    @Override
    public int getId() {
        return 0;
    }


    @Override
    public void resize(Dimension size) {
        getScaler().updateSizes(size, new Vector2D(size));
    }
}
