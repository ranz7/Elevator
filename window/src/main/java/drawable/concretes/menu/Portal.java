package drawable.concretes.menu;

import drawable.abstracts.DrawCenter;
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
        sizeSave = getSize();
        positionSave = getPosition();
        add(new ClickableButton(
                new CircleWithTextInside(getSize().divide(new Vector2D(-5, 2)).addByY(5), settings, "^"), () -> {
        }));
        add(new ClickableButton(
                new CircleWithTextInside(getSize().divide(new Vector2D(-5, 2)), settings, "+"),
                () -> {
                    startZoom();
                }));
        add(new ClickableButton(
                new CircleWithTextInside(getSize().divide(new Vector2D(-5, 2)).addByY(-5), settings, "v"),
                () -> {
                }));
        openPortal(0);
    }

    boolean startOfZoom = false;

    private void startZoom() {
        startOfZoom = !startOfZoom;
        changeCoef = 1;
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
//             changeCoef *= 0.001;
//            setPosition(positionSave.multiply(changeCoef));
//            setSize(sizeSave.multiplyX(changeCoef).add(
//                    gameDrawer.getScaler().getScreenSize().multiply(1 - changeCoef)));

            setSize(new Vector2D(
                    gameDrawer.getScaler().getFromRealToGameLength(
                            gameDrawer.getScaler().getScreenSize().getX()),
                    gameDrawer.getScaler().getFromRealToGameLength(
                            gameDrawer.getScaler().getScreenSize().getY())));
            setPosition(
                    gameDrawer.getScaler()
                            .getFromRealToGameCoordinate(new Vector2D(0, 0)
                                    , sizeSave.y));


            realDrawPosition = new Vector2D(0, 0);
        } else {
            setPosition(positionSave);
            setSize(sizeSave);
        }
        super.draw(realDrawPosition, gameDrawer);
        if (gamePlane != null) {
            Vector2D sizeOfGame = new Vector2D(
                    gameDrawer.getScaler().getFromGameToRealLength(getSize().getX()),
                    gameDrawer.getScaler().getFromGameToRealLength(getSize().getY()));
            sizeOfGame = gameDrawer.getScaler().getScreenSize();
            var sizeOfBuilding = gamePlane.getGameMap().getBuildingSize();
            if (sizeOfBuilding.length() < 1) {
                sizeOfBuilding = new Vector2D(100, 100);
            }
            gamePlane.getPainter().getScaler()
                    .updateSizes(sizeOfGame, sizeOfBuilding);

            var positionOfScaler = gameDrawer.getScaler().getFromGameToRealCoordinate(
                    getRealDrawPosition(), sizeSave.y
            );
            positionOfScaler = new Vector2D(0,0);
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
    public void add(DrawableCreature creature) {
        localDataBase.addCreature(creature);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int getDrawPriority() {
        return -2;
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

