package drawable.concretes.building.floor.elevatorSpace;

import databases.CombienedDrawDataBase;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.concretes.elevator.DrawableElevatorCreature;
import drawable.drawTool.text.Text;
import model.objects.Creature;
import tools.Vector2D;

public class ElevatorNumber extends DrawableCreature {
    private final DrawableElevatorCreature parentElevator;
    protected ElevatorNumber(Vector2D position, DrawableElevatorCreature parentElevator, CombienedDrawDataBase settings) {
        super(new Creature(position, new Vector2D(100, 15)), new Text("-", settings.colorOfNumber()), settings);
        this.parentElevator = parentElevator;
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomCenter;
    }

    @Override
    public int GetDrawPrioritet() {
        return 14;
    }

    @Override
    public void tick(double deltaTime) {
        String numberOfFloor = ((int) (parentElevator.getPosition().y / dataBase.floorHeight() + 1)) + "";
        ((Text)getTool()).setText(numberOfFloor);
    }

}
