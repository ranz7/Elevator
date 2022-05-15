package drawable.concretes.building.floor.elevatorSpace;

import databases.CombienedDrawDataBase;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.concretes.elevator.DrawableElevatorCreature;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import model.objects.Creature;
import tools.Vector2D;

public class ElevatorBorder extends DrawableCreature {
    public ElevatorBorder(Vector2D position, DrawableElevatorCreature parentElevator, CombienedDrawDataBase settings) {
        super(new Creature(position, parentElevator.getSize().getAdded(settings.borderThickness())),
                new RectangleWithBorder(settings.borderColor(), settings.borderThickness() * 2), settings);

        var buttonPosition = position.getAdded(getSize().getDivided(2)).getAddedX(settings.buttonRelativePosition());
        addSubDrawable(new ElevatorButton(buttonPosition, settings));
        addSubDrawable(new ElevatorBlackSpace(getSize(), settings));
        addSubDrawable(new ElevatorNumber(new Vector2D(2, -getSize().y + settings.borderThickness() - 3), parentElevator, settings));
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int GetDrawPrioritet() {
        return 9;
    }

}
