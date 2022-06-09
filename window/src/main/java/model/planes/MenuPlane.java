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
import java.util.Optional;
import java.util.stream.Stream;

public class MenuPlane extends Plane {

    @Getter
    private final GuiController controller;
    private final MenuDrawable menuDrawable;

    public MenuPlane(GuiController controller, LocalDrawSetting settings) {
        super(settings, new Painter(new Scaler(new Vector2D(0, 0), 1.), settings));
        this.controller = controller;
        menuDrawable = new MenuDrawable(
                controller::changeServer,
                getSettings());
        changeDoorsState(false);
    }

    public void changeDoorsState(boolean isClose) {
        menuDrawable.changeDoorsState(isClose);
    }

    @Override
    public void leftMouseClicked(Vector2D point) {
        getActivatedPortal().ifPresent(
                portal -> portal.getPlane().leftMouseClicked(point)
        );

    }

    public Optional<Portal> getActivatedPortal() {
        return getLocalDataBase().streamOf(Portal.class).filter(portal -> portal.isStartOfZoom() || portal.getExponentionalCoef() == 0).findFirst();
    }

    @Override
    public void rightMouseClicked(Vector2D point) {
        getActivatedPortal().ifPresent(
                portal -> portal.getPlane().rightMouseClicked(point)
        );
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
        var activePortal = getLocalDataBase().streamOf(Portal.class).filter(portal -> portal.isStartOfZoom()).findFirst();
        var sizeOfGame = menuDrawable.getSize();
        if (activePortal.isPresent()) {
            sizeOfGame = Vector2D.getBetween(sizeOfGame, getScaler().getScreenSize(), activePortal.get().getExponentionalCoef());
        }
        getScaler().updateGameSizes(sizeOfGame);
        super.draw(g);
    }

    public Stream<Portal> streamOfPortals() {
        return getLocalDataBase().streamOf(Portal.class);
    }
}
