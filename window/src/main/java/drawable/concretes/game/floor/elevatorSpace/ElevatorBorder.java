package drawable.concretes.game.floor.elevatorSpace;

import drawable.abstracts.Drawable;
import drawable.concretes.game.elevator.DrawableElevator;
import lombok.Getter;
import model.DatabaseOf;
import model.Transport;
import settings.CombienedDrawSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import tools.Vector2D;

public class ElevatorBorder extends DrawableCreature implements Transport {
    @Getter
    private final DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this);
    private final DrawableElevator parentElevator;

    public ElevatorBorder(Vector2D position, DrawableElevator parentElevator, CombienedDrawSettings settings) {
        super(position, parentElevator.getSize().add(settings.borderThickness()),
                new RectangleWithBorder(settings.borderColor(), settings.borderThickness() * 2), settings);
        this.parentElevator = parentElevator;
        localDataBase.add(new ElevatorBlackSpace(getSize(), settings));
        localDataBase.add(new ElevatorNumber(new Vector2D(2, -getSize().y + settings.borderThickness() - 3), settings));
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int GetDrawPrioritet() {
        return 9;
    }

    public int getCurrentFloorOfElevator() {
        return parentElevator.getCurrentFloorNum();
    }
}
