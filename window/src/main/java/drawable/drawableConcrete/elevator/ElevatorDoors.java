package drawable.drawableConcrete.elevator;

import configs.tools.CombienedDrawDataBase;
import drawable.drawableAbstract.Drawable;
import drawable.drawableAbstract.DrawableLocalMoving;
import model.objects.movingObject.trajectory.Trajectory;
import view.drawTools.drawer.GameDrawer;

import java.util.List;

public class ElevatorDoors extends DrawableLocalMoving {
    private final DrawableElevator parentElevator;

    private final ElevatorDoor leftDoor;
    private final ElevatorDoor rightDoor;

    public ElevatorDoors(DrawableElevator parentElevator, CombienedDrawDataBase settings) {
        super(parentElevator.getPosition(), parentElevator.getSize(),
                Trajectory.MomentarilyPositionChange(parentElevator.getPosition()), settings);
        this.parentElevator = parentElevator;
        leftDoor = new ElevatorDoor(getPosition(), getSize(), true, Trajectory.MomentarilyPositionChange(getPosition()), settings);
        rightDoor = new ElevatorDoor(getPosition(), getSize(), false, Trajectory.MomentarilyPositionChange(getPosition()), settings);
    }

    public void changeDoorsState(boolean newState) {
        leftDoor.changeDoorState(newState);
        rightDoor.changeDoorState(newState);
    }

    @Override
    public void draw(GameDrawer gameDrawer) {}

    @Override
    public Integer GetDrawPrioritet() {
        return 6;
    }

    public void tick(double deltaTime) {
        super.tick(deltaTime);
        leftDoor.tick(deltaTime);
        rightDoor.tick(deltaTime);
    }

    @Override
    public List<Drawable> getDrawables() {
        var drawables = super.getDrawables();
        drawables.add(leftDoor);
        drawables.add(rightDoor);
        return drawables;
    }


    @Override
    public boolean isDead() {
        return false;
    }

    public boolean isClosed() {
        return leftDoor.isClosed() && rightDoor.isClosed();
    }

}
