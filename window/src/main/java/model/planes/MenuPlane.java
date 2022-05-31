package model.planes;

import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableCreature;
import drawable.buttons.ClickableButton;
import drawable.concretes.menu.MenuDrawable;
import model.DatabaseOf;
import model.planes.graphics.Painter;
import model.planes.graphics.Scaler;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

import java.awt.*;

public class MenuPlane extends Plane {
    private final MenuDrawable menuDrawable = new MenuDrawable(getSettings());

    public MenuPlane(LocalDrawSetting settings) {
        super(settings, new Painter(new Scaler(new Vector2D(0, 0), 1.)));
    }

    boolean lol = false;

    @Override
    public void rightMouseClicked(Vector2D point) {
        menuDrawable.changeDoorsState(lol);
        lol = !lol;
//        if (zoomedIn()) {
//            var gamePosition = getScaler().getFromRealToGameCoordinate(point, 4);
//            gameMap.add(FlyingText.FlyingTextUpSlow("ZoomIn", gamePosition));
//            zoomIn(point, 1 / 4.);
//        } else {
//            zoomOut();
//        }
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
//        getScaler().updateGameSizes(menuDrawable.getSize());
    }

    @Override
    protected DatabaseOf<Drawable> getLocalDataBase() {
        return menuDrawable.getLocalDataBase();
    }

    @Override
    public void leftMouseClicked(Vector2D point) {
    }

    @Override
    public int getId() {
        return 0;
    }


    @Override
    public void resize(Dimension size) {
        getScaler().updateSizes(size, menuDrawable.getSize());
    }
}
