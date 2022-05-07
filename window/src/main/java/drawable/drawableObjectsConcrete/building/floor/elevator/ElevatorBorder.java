package drawable.drawableObjectsConcrete.building.floor.elevator;

import configs.CanvasSettings.MainSettings;
import drawable.drawableAbstract.Drawable;
import drawable.drawableAbstract.DrawableLocalCreature;
import drawable.drawableObjectsConcrete.elevator.DrawableElevator;
import lombok.Getter;
import model.GuiModel;
import model.objects.Creature;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

import java.awt.*;
import java.util.List;

public class ElevatorBorder extends DrawableLocalCreature {
    private final DrawableElevator elevatorToGetFloorFrom;
    @Getter
    ElevatorBlackSpace elevatorBlackSpace;
    @Getter
    ElevatorButton elevatorButton;

    public ElevatorBorder(Vector2D position, DrawableElevator parentElevator, MainSettings settings) {
        super(position, parentElevator.getSize().getAdded(settings.borderThickness()), settings);
        elevatorToGetFloorFrom = parentElevator;
        elevatorBlackSpace = new ElevatorBlackSpace(position, parentElevator, settings);
        elevatorButton = new ElevatorButton(position.getAdded(new Vector2D(size).getDivided(2)).getAdded(
                new Vector2D(guiModel.getMainInitializationSettings().BUTTON_RELATIVE_POSITION, 0)), guiModel);
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
        gameDrawer.drawWithBorder(this, settings.borderColor(), settings.borderThickness() * 2);
        String numberOfFloor = ((int) (elevatorToGetFloorFrom.getPosition().y / settings.floorHeight() + 1)) + "";
        Vector2D positionOfText = getPosition().getSubbed(
                new Vector2D(2, -getSize().y + settings.borderThickness() - 3));
        gameDrawer.drawText(
                numberOfFloor,
                positionOfText, 15, settings.colorOfNumber());


    }

    @Override
    public List<Drawable> getDrawables() {
        List<Drawable> drawables = super.getDrawables();
        drawables.add(elevatorBlackSpace);
        drawables.add(elevatorButton);
        return drawables;
    }
}
