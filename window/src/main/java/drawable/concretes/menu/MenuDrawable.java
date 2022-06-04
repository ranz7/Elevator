package drawable.concretes.menu;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableCreature;
import drawable.concretes.game.elevator.ElevatorDoor;
import drawable.concretes.game.floor.decorations.MenuPainting;
import drawable.drawTool.figuresComponent.Rectangle;
import lombok.Getter;
import model.DatabaseOf;
import model.Transport;
import model.objects.CreatureInterface;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

import java.util.Random;
import java.util.stream.Collectors;

public class MenuDrawable extends DrawableCreature implements Transport<Drawable> {
    @Getter
    DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this, ElevatorDoor.class,
            Portal.class,
            MenuPainting.class);

    private final ElevatorDoor leftDoor;
    private final ElevatorDoor rightDoor;

    public MenuDrawable(LocalDrawSetting localDrawSetting) {
        super(new Vector2D(0, 0), new Vector2D(150, 75), new Rectangle(localDrawSetting.florBetonColor()),
                localDrawSetting);
        leftDoor = new ElevatorDoor(
                new Vector2D(0, 0), this.getSize(),
                true, localDrawSetting, 2000, 999);
        rightDoor = new ElevatorDoor(
                new Vector2D(this.getSize().x, 0), this.getSize(),
                false, localDrawSetting, 2000, 999);

        add(leftDoor);
        add(rightDoor);

//        add(new ElevatorBorder(new Vector2D(300,0),new Vector2D(200,400),));
    }

    public void changeDoorsState(boolean state) {
        leftDoor.changeDoorState(state);
        rightDoor.changeDoorState(state);
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
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
                    + portalCount * getSettings().portalSize().x * 1.5, getSettings().portalSize().y/2),
                    getSettings(),
                    new Random()));
        } else {
            add(new MenuPainting(new Vector2D(getSettings().portalSize().getX() / 2
                    + portalCount * getSettings().portalSize().x * 1.5, getSettings().portalSize().y*1.2),
                    getSettings(),
                    new Random()));
            add(new MenuPainting(new Vector2D(getSettings().portalSize().getX() / 2+20
                    + portalCount * getSettings().portalSize().x * 1.5, getSettings().portalSize().y*1.7),
                    getSettings(),
                    new Random()));
            add(new MenuPainting(new Vector2D(getSettings().portalSize().getX() / 2
                    + portalCount * getSettings().portalSize().x * 1.5, getSettings().portalSize().y*2.3),
                    getSettings(),
                    new Random()));
            localDataBase.addCreature(new Portal(getSettings().portalSize().getX() / 2
                    + portalCount * getSettings().portalSize().x * 1.5, getSettings()));
        }
        portalCount++;
        setSize(getSize().multiply(1.25));
        leftDoor.updateSize(this.getSize());
        rightDoor.updateSize(this.getSize());
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
            var objects = localDataBase.streamOf(MenuPainting.class).collect(Collectors.toList());
            objects.get(objects.size()-1).setDead(true);
            objects.get(objects.size()-2).setDead(true);
            objects.get(objects.size()-3).setDead(true);
            localDataBase.streamOf(Portal.class)
                    .reduce((first, second) -> second)
                    .orElse(null).setDead(true);
        }
        setSize(getSize().multiply(0.8));
        leftDoor.updateSize(this.getSize());
        rightDoor.updateSize(this.getSize());

    }
}
