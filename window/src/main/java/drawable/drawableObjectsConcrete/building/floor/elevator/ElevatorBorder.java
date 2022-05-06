package drawable.drawableObjectsConcrete.building.floor.elevator;

import drawable.drawableBase.Drawable;
import lombok.Getter;
import model.GuiModel;
import model.objects.Creature;
import tools.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;
import java.util.List;

public class ElevatorBorder extends Drawable {
    private final Creature PARENT_ELEVATOR;
    Color BORDER_COLOR;
    Color NUMBER_COLOR;
    int BORDER_SIZE;
    Double WALL_HEIGHT;
    @Getter
    ElevatorBlackSpace elevatorBlackSpace;
    @Getter
    ElevatorButton elevatorButton;

    public ElevatorBorder(Vector2D position, Creature parentElevator, GuiModel guiModel) {
        super(position, parentElevator.getSize());
        PARENT_ELEVATOR = parentElevator;
        BORDER_COLOR = guiModel.colorSettings.elevatorBorder;
        NUMBER_COLOR = guiModel.colorSettings.elevatorCurrentFloor;
        BORDER_SIZE = guiModel.drawSettings.ELEVATOR_BORDER_THICKNESS;
        WALL_HEIGHT = guiModel.getWallHeight();

        size = new Vector2D(size.x + guiModel.drawSettings.ELEVATOR_BORDER_THICKNESS * 2,
                size.y + guiModel.drawSettings.ELEVATOR_BORDER_THICKNESS);


        elevatorBlackSpace = new ElevatorBlackSpace(position, parentElevator, guiModel);
        elevatorButton = new ElevatorButton(position.getAdded(new Vector2D(size).getDivided(2)).getAdded(
                new Vector2D(guiModel.getMainInitializationSettings().BUTTON_RELATIVE_POSITION, 0)), guiModel);
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 9;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (!PARENT_ELEVATOR.isVisible()) {
            return;
        }
        gameDrawer.setColor(BORDER_COLOR);
        gameDrawer.drawRect(this.position, this.size, BORDER_SIZE * 2);
        gameDrawer.setFont("TimesRoman", Font.PLAIN, 15);
        var positionOfText = position;
        gameDrawer.setColor(NUMBER_COLOR);
        gameDrawer.draw(
                ((int) (PARENT_ELEVATOR.getPosition().y / WALL_HEIGHT + 1)) + "", positionOfText.getAdded(
                        new Vector2D(-2, size.y - BORDER_SIZE + 3)));
    }

    @Override
    public List<Drawable> getDrawables() {
        List<Drawable> drawables = super.getDrawables();
        drawables.add(elevatorBlackSpace);
        drawables.add(elevatorButton);
        return drawables;
    }
}
