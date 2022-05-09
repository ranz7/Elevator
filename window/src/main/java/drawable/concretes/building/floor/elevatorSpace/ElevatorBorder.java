package drawable.concretes.building.floor.elevatorSpace;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.Drawable;
import drawable.abstracts.withShape.creatures.DrawableLocalCreature;
import drawable.concretes.elevator.DrawableElevator;
import drawable.drawTool.text.TextData;
import lombok.Getter;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

import java.util.List;

public class ElevatorBorder extends DrawableLocalCreature {
    private final DrawableElevator elevatorToGetFloorFrom;
    @Getter
    ElevatorButton elevatorButton;

    DrawableLocalText elevatorNumber;
    ElevatorBlackSpace elevatorBlackSpace;

    public ElevatorBorder(Vector2D position, DrawableElevator parentElevator, CombienedDrawDataBase settings) {
        super(position, parentElevator.getSize().getAdded(settings.borderThickness()), settings);
        elevatorToGetFloorFrom = parentElevator;
        elevatorBlackSpace = new ElevatorBlackSpace(position, getSize(), settings);
        var buttonPosition =
                position.getAdded(getSize().getDivided(2))
                        .getAddedX(settings.buttonRelativePosition());
        elevatorButton = new ElevatorButton(buttonPosition, settings);

        Vector2D positionOfText = getPosition().getSubbed(
                new Vector2D(2, -getSize().y + settings.borderThickness() - 3));
        elevatorNumber = new DrawableLocalText("NULL",
                positionOfText, new TextData(15, settings.colorOfNumber(), Trajectory.StaticPoint()));
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 9;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (!elevatorToGetFloorFrom.getIsVisible()) {
            return;
        }
        gameDrawer.draw(this, settings.borderColor(), settings.borderThickness() * 2);
        String numberOfFloor = ((int) (elevatorToGetFloorFrom.getPosition().y / settings.floorHeight() + 1)) + "";
        elevatorNumber.setText(numberOfFloor);
    }

    @Override
    public List<Drawable> getDrawables() {
        List<Drawable> drawables = super.getDrawables();
        drawables.add(elevatorBlackSpace);
        drawables.add(elevatorButton);
        drawables.add(elevatorNumber);
        return drawables;
    }
}
