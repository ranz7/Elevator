package drawable.concretes.elevator;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreatureRemote;
import drawable.drawTool.figuresComponent.Rectangle;
import model.objects.Creature;
import tools.Vector2D;
import lombok.Getter;

@Getter
public class DrawableCreatureElevator extends DrawableCreatureRemote {
    ElevatorDoor leftDoor;
    ElevatorDoor rightDoor;

    public DrawableCreatureElevator(Creature creature, CombienedDrawDataBase settings) {
        super(creature, new Rectangle(settings.elevatorBackGroundColor()), settings);
        leftDoor = new ElevatorDoor(
                new Vector2D(0, 0), this.getSize().getDividedX(2), true, settings);
        rightDoor = new ElevatorDoor(
                new Vector2D(this.getSize().x / 2, 0), this.getSize().getDividedX(2), false, settings);
        addSubDrawable(leftDoor);
        addSubDrawable(rightDoor);
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
}
