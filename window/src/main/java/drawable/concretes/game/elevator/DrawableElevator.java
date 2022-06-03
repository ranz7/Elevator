package drawable.concretes.game.elevator;

import drawable.abstracts.Drawable;
import drawable.concretes.game.floor.DrawableFloorStructure;
import lombok.Setter;
import model.DatabaseOf;
import model.Transport;
import model.Transportable;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableRemoteCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;
import lombok.Getter;

@Getter
public class DrawableElevator extends DrawableRemoteCreature implements Transport<Drawable>, Transportable<Drawable>, FloorGetter{
    @Setter
    @Getter
    private Transport<Drawable> transport;
    @Getter
    private final DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this, ElevatorDoor.class);

    private final ElevatorDoor leftDoor;
    private final ElevatorDoor rightDoor;


    public DrawableElevator(double OpenCloseTime, LocalDrawSetting settings) {
        super(new Rectangle(settings.elevatorBackGroundColor()), settings);
        leftDoor = new ElevatorDoor(
                new Vector2D(0, 0), this.getSize(),
                true, settings, OpenCloseTime);
        rightDoor = new ElevatorDoor(
                new Vector2D(this.getSize().x / 2, 0), this.getSize(),
                false, settings, OpenCloseTime);

        add(leftDoor);
        add(rightDoor);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomCenter;
    }

    @Override
    public int getDrawPriority() {
        return 4;
    }

    public void changeDoorsState(boolean state) {
        leftDoor.changeDoorState(state);
        rightDoor.changeDoorState(state);
    }

    public int getCurrentFloorNum() {
        return ((DrawableFloorStructure) transport).getCurrentFloorNum();
    }

    @Override
    public void add(Drawable drawable) {
        localDataBase.addCreature(drawable);
    }
}
