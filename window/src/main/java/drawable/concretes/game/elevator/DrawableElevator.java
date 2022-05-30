package drawable.concretes.game.elevator;

import drawable.abstracts.Drawable;
import drawable.concretes.game.floor.DrawableFloorStructure;
import lombok.Setter;
import model.DatabaseOf;
import model.Transport;
import model.Transportable;
import settings.CombienedDrawSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableRemoteCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import tools.Vector2D;
import lombok.Getter;

@Getter
public class DrawableElevator extends DrawableRemoteCreature implements Transport, Transportable {
    @Setter
    @Getter
    private Transport transport;
    @Getter
    private final DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this);

    private final ElevatorDoor leftDoor;
    private final ElevatorDoor rightDoor;


    public DrawableElevator(CombienedDrawSettings settings) {
        super(new Rectangle(settings.elevatorBackGroundColor()), settings);
        leftDoor = new ElevatorDoor(
                new Vector2D(0, 0), this.getSize().divideByX(2),
                true, settings,
                getSettings().elevatorOpenCloseTime());
        rightDoor = new ElevatorDoor(
                new Vector2D(this.getSize().x / 2, 0), this.getSize().divideByX(2),
                false, settings,
                getSettings().elevatorOpenCloseTime());

        localDataBase.add(leftDoor);
        localDataBase.add(rightDoor);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomCenter;
    }

    @Override
    public int GetDrawPrioritet() {
        return 4;
    }

    public void changeDoorsState(boolean state) {
        leftDoor.changeDoorState(state);
        rightDoor.changeDoorState(state);
    }

    public int getCurrentFloorNum() {
        return ((DrawableFloorStructure) transport).getCurrentFloorNum();
    }
}
