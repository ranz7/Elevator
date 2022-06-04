package drawable.concretes.game.floor.elevatorSpace;

import drawable.concretes.game.elevator.FloorGetter;
import lombok.Getter;
import model.DatabaseOf;
import model.Transport;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

public class ElevatorBorder extends DrawableCreature implements Transport<DrawableCreature> {
    @Getter
    private final DatabaseOf<DrawableCreature> localDataBase = new DatabaseOf<>(this,
            ElevatorBlackSpace.class, ElevatorNumber.class);
    private final FloorGetter parentElevator;

    public ElevatorBorder(Vector2D position,Vector2D elevatorSize, FloorGetter parentElevator, LocalDrawSetting settings) {
        super(position, elevatorSize.add(settings.borderThickness()),
                new RectangleWithBorder(settings.borderColor(), settings.borderThickness() * 2), settings);
        this.parentElevator = parentElevator;
        add(new ElevatorBlackSpace(getSize(), settings));
        add(new ElevatorNumber(new Vector2D(2, -getSize().y + settings.borderThickness() - 3), settings));
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int getDrawPriority() {
        return 9;
    }

    public int getCurrentFloorOfElevator() {
        return parentElevator.getCurrentFloorNum();
    }

    @Override
    public void add(DrawableCreature drawableCreature) {
        localDataBase.addCreature(drawableCreature);
    }
}
