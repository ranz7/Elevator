package drawable.concretes.menu;

import drawable.RainbowColor;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableCreature;
import drawable.buttons.CircleWithTextInside;
import drawable.buttons.ClickableButton;
import drawable.concretes.game.elevator.FloorGetter;
import drawable.concretes.game.floor.elevatorSpace.ElevatorNumber;
import drawable.drawTool.figuresComponent.Rectangle;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import lombok.Getter;
import lombok.Setter;
import model.DatabaseOf;
import model.GameMap;
import model.Transport;
import model.Transportable;
import model.planes.GamePlane;
import model.planes.graphics.Painter;
import settings.localDraw.LocalDrawSetting;
import tools.MathFunctions;
import tools.Vector2D;

import java.awt.*;
import java.util.function.Function;

public class Portal extends DrawableCreature implements Transport<Drawable>, Transportable<Drawable>, FloorGetter {
    @Getter
    DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this, ClickableButton.class,
            ElevatorNumber.class);
    @Getter
    @Setter
    Transport<Drawable> transport;

    @Getter
    private GamePlane gamePlane;

    @Getter
    private int roomId = -1;
    ClickableButton exitButton;

    public Portal(double positionX, LocalDrawSetting settings) {
        super(new Vector2D(positionX, 0), settings.portalSize(),
                new RectangleWithBorder(
                        settings.portalColor(),
                        new Color(255, 201, 0),
                        1), settings);
        originalSize = getSize();
        originalPosition = getPosition();

        rainbow = new RainbowColor(getTool().getMainColor());
        add(new ClickableButton(
                new CircleWithTextInside(getSize().divide(new Vector2D(-5, 2)).addByY(5), settings, "^"), () -> {
            roomId++;
        }));

        add(new ClickableButton(
                new CircleWithTextInside(getSize().divide(new Vector2D(-5, 2)), settings, "+"),
                this::changeZoom));

        add(new ClickableButton(
                new CircleWithTextInside(getSize().divide(new Vector2D(-5, 2)).addByY(-5), settings, "v"),
                () -> {
                    roomId--;
                    roomId = Math.max(roomId, 0);
                }));

        add(new ElevatorNumber(getSize().divideByX(2), this, getSettings()));

        exitButton = new ClickableButton(
                new CircleWithTextInside(new Vector2D(15, 15), settings, "x"),
                this::changeZoom);
        add(exitButton);
        exitButton.setVisible(false);
        openPortal(0);
    }

    @Getter

    boolean startOfZoom = false;
    Function<Double, Double> expo = MathFunctions.randomFunction();

    public void changeZoom() {
        startOfZoom = !startOfZoom;
        expo = MathFunctions.randomFunction();

        if (!startOfZoom) {
            ((MenuDrawable) transport).portalWasClosed();
            gamePlane.getGameMap().removeButtons();
        }
    }

    private void openPortal(int roomId) {
        this.roomId = roomId;
        // model will check and create portal for us
    }

    private Vector2D originalSize;
    private Vector2D originalPosition;


    private double changeCoef = 1;

    @Getter
    private double exponentionalCoef = 1;
    private RainbowColor rainbow;

    @Override
    public void draw(Vector2D realDrawPosition, Painter gameDrawer) {
        exponentionalCoef = expo.apply(changeCoef);
        setSize(Vector2D.getBetween(originalSize, new Vector2D(gameDrawer.getScaler().getGameSize()), exponentionalCoef));
        setPosition(Vector2D.getBetween(originalPosition, new Vector2D(0, 0), changeCoef));
//        getTool().setColor(getTool().getMainColor().);
        super.draw(realDrawPosition, gameDrawer);
        if (startOfZoom) {
            changeCoef = Math.max(0, changeCoef - 0.005);
            if (changeCoef < 0.010 && changeCoef > 0) {
                ((MenuDrawable) transport).portalWasOpened();
                setVisible(true);
                gamePlane.getGameMap().initializeButtons(this);
                changeCoef = 0;
            }

        } else {
            changeCoef = Math.min(1, changeCoef + 0.005);
        }

        setSize(Vector2D.getBetween(originalSize, new Vector2D(
                gameDrawer.getScaler().getFromRealToGameLength(
                        gameDrawer.getScaler().getScreenSize().getX()),
                gameDrawer.getScaler().getFromRealToGameLength(
                        gameDrawer.getScaler().getScreenSize().getY())), exponentionalCoef));
        setPosition(Vector2D.getBetween(originalPosition,
                gameDrawer.getScaler()
                        .getFromRealToGameCoordinate(new Vector2D(0, 0)
                                , originalSize.y), exponentionalCoef));


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
                    startDrawOfSubPlane, originalSize.y
            );
            positionOfScaler = Vector2D.getBetween(positionOfScaler, new Vector2D(0, 0), changeCoef);

            gameDrawer.getPureGraphics().translate(
                    (int) positionOfScaler.getX(),
                    (int) positionOfScaler.getY()
            );
            gamePlane.getPainter().setBlackSpaces(1 - changeCoef);
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
        getTool().setColor(rainbow.next());

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

    @Override
    public int getCurrentFloorNum() {
        return roomId;
    }

}

