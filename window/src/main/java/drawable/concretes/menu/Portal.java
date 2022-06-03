package drawable.concretes.menu;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableCreature;
import drawable.buttons.CircleWithTextInside;
import drawable.buttons.ClickableButton;
import drawable.drawTool.figuresComponent.Rectangle;
import lombok.Getter;
import lombok.Setter;
import model.DatabaseOf;
import model.GameMap;
import model.Transport;
import model.Transportable;
import model.planes.GamePlane;
import model.planes.MenuPlane;
import model.planes.graphics.Painter;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

import java.awt.*;

public class Portal extends DrawableCreature implements Transport<DrawableCreature>, Transportable<DrawableCreature> {
    @Getter
    DatabaseOf<DrawableCreature> localDataBase = new DatabaseOf<>(this, ClickableButton.class);
    @Getter
    @Setter
    Transport<DrawableCreature> transport;

    private GamePlane gamePlane;

    @Getter
    private int roomId = -1;

    public Portal(double positionX, LocalDrawSetting settings) {
        super(new Vector2D(positionX, 0), settings.portalSize(), new Rectangle(new Color(0, 139, 203)), settings);
        add(new ClickableButton(
                new CircleWithTextInside(getSize().divide(new Vector2D(-5, 2)), settings)));
        add(new ClickableButton(
                new CircleWithTextInside(getSize().divide(new Vector2D(-5, 2)).addByY(5), settings)));
        add(new ClickableButton(
                new CircleWithTextInside(getSize().divide(new Vector2D(-5, 2)).addByY(-5), settings)));
        openPortal(0);
    }

    private void openPortal(int roomId) {
        this.roomId = roomId;
        // model will check and create portal for us
    }

    @Override
    public void draw(Vector2D realDrawPosition, Painter gameDrawer) {
        super.draw(realDrawPosition, gameDrawer);
        if (gamePlane != null) {
            gamePlane.getGameMap().draw(realDrawPosition, gameDrawer);
        }
    }

    @Override
    public void add(DrawableCreature creature) {
        localDataBase.addCreature(creature);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int getDrawPriority() {
        return -9;
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
        if (gamePlane != null) {
            gamePlane.tick(deltaTime);
        }
    }

    public void setGameMap(GameMap gameMap) {
        if (gamePlane == null) {
            gamePlane = new GamePlane(getSettings(), gameMap);
            return;
        }
        if (gamePlane.getGameMap().getRoomRemoteSettings().roomId() != gameMap.getRoomRemoteSettings().roomId()) {
            gamePlane = new GamePlane(getSettings(), gameMap);
        }
    }

    public GamePlane getPlane() {
        return gamePlane;
    }

    public void removeGameMap() {
        gamePlane = null;
    }

}

