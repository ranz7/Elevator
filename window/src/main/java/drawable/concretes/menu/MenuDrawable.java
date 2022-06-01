package drawable.concretes.menu;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableCreature;
import drawable.buttons.CircleWithTextInside;
import drawable.buttons.ClickableButton;
import drawable.concretes.game.elevator.ElevatorDoor;
import drawable.drawTool.figuresComponent.Rectangle;
import lombok.Getter;
import model.DatabaseOf;
import model.Transport;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

public class MenuDrawable extends DrawableCreature implements Transport<Drawable> {
    @Getter
    DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this, ElevatorDoor.class, ClickableButton.class);

    private final ElevatorDoor leftDoor;
    private final ElevatorDoor rightDoor;

    public MenuDrawable(LocalDrawSetting localDrawSetting) {
        super(new Vector2D(0, 0), new Vector2D(800, 390), new Rectangle(localDrawSetting.florBetonColor()),
                localDrawSetting);
        leftDoor = new ElevatorDoor(
                new Vector2D(0, 0), this.getSize(),
                true, localDrawSetting, 2000);
        rightDoor = new ElevatorDoor(
                new Vector2D(this.getSize().x, 0), this.getSize(),
                false, localDrawSetting, 2000);

        add(leftDoor);
        add(rightDoor);
        add(new ClickableButton(new CircleWithTextInside(getSize().divide(new Vector2D(5, 2)), localDrawSetting)));
    }

    public void changeDoorsState(boolean state) {
        leftDoor.changeDoorState(state);
        rightDoor.changeDoorState(state);
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

    @Override
    public void add(Drawable drawable) {
        localDataBase.addCreature(drawable);
    }
}
