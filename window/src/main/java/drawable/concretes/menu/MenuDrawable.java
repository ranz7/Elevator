package drawable.concretes.menu;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableCreature;
import drawable.buttons.CircleWithTextInside;
import drawable.buttons.ClickableButton;
import drawable.concretes.game.elevator.ElevatorDoor;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import lombok.Getter;
import model.DatabaseOf;
import model.Transport;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

import java.awt.*;
import java.util.Random;


public class MenuDrawable extends DrawableCreature implements Transport {
    @Getter
    DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this);

    private final ElevatorDoor leftDoor;
    private final ElevatorDoor rightDoor;

    public MenuDrawable(LocalDrawSetting localDrawSetting) {
        super(new Vector2D(0, 0), new Vector2D(800, 400),
                new RectangleWithBorder(localDrawSetting.florBetonColor(),
                        new Color(0, 0, 0), 7),
                localDrawSetting);
        leftDoor = new ElevatorDoor(
                new Vector2D(0, 0), this.getSize(),
                true, localDrawSetting, 3000);
        rightDoor = new ElevatorDoor(
                new Vector2D(this.getSize().x, 0), this.getSize(),
                false, localDrawSetting, 3000);

        localDataBase.add(leftDoor);
        localDataBase.add(rightDoor);

       /* localDataBase.add(
                new ClickableButton(
                        new CircleWithTextInside(
                                new Vector2D(133, 233),
                                localDrawSetting)));
        localDataBase.add(
                new ClickableButton(
                        new CircleWithTextInside(
                                new Vector2D(123, 133),
                                localDrawSetting)));*/
        localDataBase.add(
                new ClickableButton(
                        new CircleWithTextInside(
                                new Vector2D(142, 52),
                                localDrawSetting),()->{}));
    }

    public void changeDoorsState(boolean state) {
        int type = new Random().nextInt(5);
        leftDoor.changeDoorState(state, type);
        rightDoor.changeDoorState(state, type);
    }

    @Override
    public void tick(double deltaTime) {
        System.out.println("EHO");
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int getDrawPrioritet() {
        return 10;
    }
}
