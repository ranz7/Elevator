package model.planes;

import controller.GuiController;
import drawable.abstracts.Drawable;
import drawable.concretes.menu.MenuDrawable;
import drawable.concretes.menu.Portal;
import lombok.Getter;
import model.DatabaseOf;
import model.planes.graphics.Painter;
import model.planes.graphics.Scaler;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class MenuPlane extends Plane {
    private final MenuDrawable menuDrawable = new MenuDrawable(getSettings());

    @Getter
    private final GuiController controller;

    public MenuPlane(GuiController controller, LocalDrawSetting settings) {
        super(settings, new Painter(new Scaler(new Vector2D(0, 0), 1.)));
        this.controller = controller;
        changeDoorsState(false);
    }

    public void changeDoorsState(boolean isClose) {
        menuDrawable.changeDoorsState(isClose);
    }

    @Override
    public void leftMouseClicked(Vector2D point) {

    }

    @Override
    public void rightMouseClicked(Vector2D point) {
//        controller.addRoom(counter++);

        //lol = !lol;
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
        menuDrawable.tick(deltaTime);

        updateDrawingObjectsForThreadSafety();
    }

    @Override
    protected DatabaseOf<Drawable> getLocalDataBase() {
        return menuDrawable.getLocalDataBase();
    }

    @Override
    public void resize(Vector2D size) {
        getScaler().updateSizes(size, menuDrawable.getSize());
    }



    public List<Integer> getUsedGamePlanesInPortals() {
        List<Integer> rooms = new LinkedList<>();
        getLocalDataBase().streamOf(Portal.class).forEach(portal -> {
                    if (portal.getRoomId() != -1) {
                        rooms.add(portal.getRoomId());
                    }
                }
        );
        return rooms;
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        getScaler().updateGameSizes(menuDrawable.getSize());
    }

    public Stream<Portal> streamOfPortals() {
        return getLocalDataBase().streamOf(Portal.class);
    }
}
