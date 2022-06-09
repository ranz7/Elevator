package drawable.concretes.menu;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableCreature;
import drawable.buttons.ClickableButton;
import drawable.buttons.RectangleWithTextInside;
import drawable.buttons.RectangleWithTextInside2;
import drawable.concretes.game.elevator.ElevatorDoor;
import drawable.concretes.game.floor.decorations.MenuPainting;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import lombok.Getter;
import model.DatabaseOf;
import model.Transport;
import model.objects.CreatureInterface;
import model.planes.graphics.Painter;
import settings.localDraw.LocalDrawSetting;
import tools.Timer;
import tools.Vector2D;
import view.buttons.MutableColor;

import java.awt.*;
import java.util.Random;

public class MenuDrawable extends DrawableCreature implements Transport<Drawable> {
    @Getter
    DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this, ElevatorDoor.class,
            Portal.class,
            ClickableButton.class,
            MenuPainting.class);

    private final ElevatorDoor leftDoor;
    private final ElevatorDoor rightDoor;
    private final ClickableButton leftDoorButton;
    private final ClickableButton rightDoorButton;

    double sizeOfLeftRightButtonsInProportion = 1. / 16;

    public MenuDrawable(Runnable changeServer, LocalDrawSetting localDrawSetting) {
        super(new Vector2D(0, 0), new Vector2D(150, 75),
                new RectangleWithBorder(localDrawSetting.florBetonColor(),
                        new MutableColor(0, 0, 0), 3),
                localDrawSetting);
        secondTimer.turnOff();
        this.leftDoorButton = new ClickableButton(new RectangleWithTextInside(
                getSize().divide(2).setX(getSize().multiplyX(sizeOfLeftRightButtonsInProportion / 4).x),
                getSize().multiplyX(sizeOfLeftRightButtonsInProportion / 2),
                localDrawSetting, "<"),
                () -> {
                    changeDoorsState(true);
                    ifClosed(this::removePortal);
                });

        this.rightDoorButton = new ClickableButton(new RectangleWithTextInside(
                new Vector2D(this.getSize().x - getSize().multiplyX(sizeOfLeftRightButtonsInProportion / 4).getX(), getSize().getY() / 2),
                getSize().multiplyX(sizeOfLeftRightButtonsInProportion / 2), localDrawSetting, ">"
        ), () -> {
            changeDoorsState(true);
            ifClosed(this::createPortal);
        });

        leftDoor = new ElevatorDoor(
                new Vector2D(0, 0), this.getSize(),
                true, localDrawSetting, 3000, 999);
        rightDoor = new ElevatorDoor(
                new Vector2D(this.getSize().x, 0), this.getSize(),
                false, localDrawSetting, 3000, 999);


        add(leftDoor);
        add(rightDoorButton);
        add(leftDoorButton);
        add(rightDoor);

        add( new ClickableButton(new RectangleWithTextInside2(
                new Vector2D(15,5),
                new Vector2D(15,5),
                localDrawSetting, "RemoteServer"),
                changeServer,
                true)
        );
    }

    private Runnable toExecuteIfClosed;

    private void ifClosed(Runnable toExecuteAfterClose) {
        timer.restart();
        this.toExecuteIfClosed = toExecuteAfterClose;
    }

    public void changeDoorsState(boolean isClosed) {
        int type = new Random().nextInt(5);
        leftDoor.changeDoorState(isClosed, type);
        rightDoor.changeDoorState(isClosed, type);
    }

    @Override
    public void draw(Vector2D realDrawPosition, Painter gameDrawer) {
        super.draw(realDrawPosition, gameDrawer);
    }

    Timer timer = new Timer(3200);
    Timer secondTimer = new Timer(300);

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);

        leftDoor.updateSize(this.getSize());
        rightDoor.updateSize(this.getSize());


        leftDoorButton.getParasite().setPosition(getSize().divide(2)
                .setX(getSize().multiplyX(sizeOfLeftRightButtonsInProportion / 4).x));
        leftDoorButton.getParasite().setSize(getSize().multiplyX(sizeOfLeftRightButtonsInProportion / 2));

        rightDoorButton.getParasite().setPosition(new Vector2D(this.getSize().x - getSize()
                .multiplyX(sizeOfLeftRightButtonsInProportion / 4).getX(),
                getSize().getY() / 2.));
        rightDoorButton.getParasite().setSize(getSize().multiplyX(sizeOfLeftRightButtonsInProportion / 2));


        timer.tick(deltaTime);
        secondTimer.tick(deltaTime);
        if (leftDoor.isClosed() && toExecuteIfClosed != null) {
            if (timer.isReady()) {
                toExecuteIfClosed.run();
                toExecuteIfClosed = null;
                secondTimer.restart();
            }
        }
        if (secondTimer.isReady()) {
            changeDoorsState(false);
            secondTimer.turnOff();
        }
        getLocalDataBase().tick(deltaTime);


        getLocalDataBase().removeIf(CreatureInterface::isDead);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int getDrawPriority() {
        return -10;
    }

    @Override
    public void add(Drawable drawable) {
        localDataBase.addCreature(drawable);
    }

    int portalCount = 0;

    public void createPortal() {
        if (portalCount == 6) {
            return;
        }
        if (portalCount == 5) {
            add(new MenuPainting(new Vector2D(getSettings().portalSize().getX() / 2
                    + portalCount * getSettings().portalSize().x * 1.5, getSettings().portalSize().y / 2),
                    getSettings(),
                    new Random()));
        } else {
            add(new MenuPainting(new Vector2D(getSettings().portalSize().getX() / 2
                    + portalCount * getSettings().portalSize().x * 1.5, getSettings().portalSize().y * 1.2),
                    getSettings(),
                    new Random()));
            add(new MenuPainting(new Vector2D(getSettings().portalSize().getX() / 2 + 20
                    + portalCount * getSettings().portalSize().x * 1.5, getSettings().portalSize().y * 1.7),
                    getSettings(),
                    new Random()));
            add(new MenuPainting(new Vector2D(getSettings().portalSize().getX() / 2
                    + portalCount * getSettings().portalSize().x * 1.5, getSettings().portalSize().y * 2.3),
                    getSettings(),
                    new Random()));
            localDataBase.addCreature(new Portal(getSettings().portalSize().getX() / 2
                    + portalCount * getSettings().portalSize().x * 1.5, getSettings()));
        }
        portalCount++;
        setSize(getSize().multiply(1.25));
    }

    public void removePortal() {
        if (portalCount == 0) {
            return;
        }
        portalCount--;

        if (portalCount == 5) {
            localDataBase.streamOf(MenuPainting.class)
                    .reduce((first, second) -> second)
                    .orElse(null).setDead(true);
        } else {
            var objects = localDataBase.streamOf(MenuPainting.class).toList();
            objects.get(objects.size() - 1).setDead(true);
            objects.get(objects.size() - 2).setDead(true);
            objects.get(objects.size() - 3).setDead(true);
            localDataBase.streamOf(Portal.class)
                    .reduce((first, second) -> second)
                    .orElse(null).setDead(true);
        }
        setSize(getSize().multiply(0.8));
    }


    public void portalWasOpened() {
        localDataBase.streamTrio().forEach(trio -> trio.getThird().setVisible(false));
    }


    public void portalWasClosed() {
        localDataBase.streamTrio().forEach(trio -> trio.getThird().setVisible(true));
    }
}
