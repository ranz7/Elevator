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
import model.planes.graphics.Painter;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

import java.awt.*;

public class Portal extends DrawableCreature implements Transport<Drawable>, Transportable<Drawable> {
    @Getter
    DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this, ClickableButton.class);
    @Getter
    @Setter
    Transport<Drawable> transport;

    private GamePlane gamePlane;

    @Getter
    private int roomId = -1;
    ClickableButton exitButton;

    public Portal(double positionX, LocalDrawSetting settings) {
        super(new Vector2D(positionX, 0), settings.portalSize(), new Rectangle(new Color(0, 139, 203)), settings);
        sizeSave = getSize();
        positionSave = getPosition();
        add(new ClickableButton(
                new CircleWithTextInside(getSize().divide(new Vector2D(-5, 2)).addByY(5), settings, "^"), () -> {
        }));
        add(new ClickableButton(
                new CircleWithTextInside(getSize().divide(new Vector2D(-5, 2)), settings, "+"),
                () -> {
                    System.out.println("GEG");
                    changeZoom();
                }));
        add(new ClickableButton(
                new CircleWithTextInside(getSize().divide(new Vector2D(-5, 2)).addByY(-5), settings, "v"),
                () -> {
                }));
        exitButton = new ClickableButton(
                new CircleWithTextInside(new Vector2D(15, 15), settings, "x"),
                () -> {
                    System.out.println("KEK");
                    changeZoom();
                });
        add(exitButton);
        exitButton.setVisible(false);
        openPortal(0);
    }

    boolean startOfZoom = false;

    private void changeZoom() {
        startOfZoom = !startOfZoom;
        changeCoef = 1;
        if (!startOfZoom) {
            ((MenuDrawable) transport).portalWasClosed();
        }
    }

    private void openPortal(int roomId) {
        this.roomId = roomId;
        // model will check and create portal for us
    }

    private Vector2D sizeSave;
    private Vector2D positionSave;
    private double changeCoef = 0;

    @Override
    public void draw(Vector2D realDrawPosition, Painter gameDrawer) {
        if (startOfZoom) {
            setSize(Vector2D.getBetween(sizeSave, new Vector2D(gameDrawer.getScaler().getGameSize()), changeCoef));
            setPosition(Vector2D.getBetween(positionSave, new Vector2D(0, 0), changeCoef));
        }
//        getTool().setColor(getTool().getMainColor().);
        super.draw(realDrawPosition, gameDrawer);
        if (startOfZoom) {
            changeCoef = changeCoef - changeCoef / 50;
            if (changeCoef < 0.0001 && changeCoef > 0) {
                ((MenuDrawable) transport).portalWasOpened();
                setVisible(true);
                changeCoef = 0;
            }
            setSize(Vector2D.getBetween(sizeSave, new Vector2D(
                    gameDrawer.getScaler().getFromRealToGameLength(
                            gameDrawer.getScaler().getScreenSize().getX()),
                    gameDrawer.getScaler().getFromRealToGameLength(
                            gameDrawer.getScaler().getScreenSize().getY())), changeCoef));
            setPosition(Vector2D.getBetween(positionSave,
                    gameDrawer.getScaler()
                            .getFromRealToGameCoordinate(new Vector2D(0, 0)
                                    , sizeSave.y), changeCoef));

        } else {
            setPosition(positionSave);
            setSize(sizeSave);
        }
        if (gamePlane != null) {
            Vector2D sizeOfGame = new Vector2D(
                    gameDrawer.getScaler().getFromGameToRealLength(getSize().getX()),
                    gameDrawer.getScaler().getFromGameToRealLength(getSize().getY()));
            var sizeOfBuilding = gamePlane.getGameMap().getBuildingSize();
            if (sizeOfBuilding.length() < 1) {
                sizeOfBuilding = new Vector2D(100, 100);
            }
            gamePlane.getPainter().getScaler()
                    .updateSizes(sizeOfGame, sizeOfBuilding);
            var startDrawOfSubPlane = getRealDrawPosition();
            var positionOfScaler = gameDrawer.getScaler().getFromGameToRealCoordinate(
                    startDrawOfSubPlane, sizeSave.y
            );
            if (startOfZoom) {
                positionOfScaler = Vector2D.getBetween(positionOfScaler, new Vector2D(0, 0), changeCoef);
            }
            gameDrawer.getPureGraphics().translate(
                    (int) positionOfScaler.getX(),
                    (int) positionOfScaler.getY()
            );
            gamePlane.draw(gameDrawer.getPureGraphics());
            gamePlane.getPainter().getPureGraphics().translate(
                    -(int) positionOfScaler.getX(),
                    -(int) positionOfScaler.getY()
            );
        }
    }

    @Override
    public void add(Drawable creature) {
        localDataBase.addCreature(creature);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int getDrawPriority() {
        if (startOfZoom) {
            return 1000;
        }
        return -2;
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
        exitButton.setVisible(startOfZoom);
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

