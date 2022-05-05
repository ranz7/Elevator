package drawable.drawableObjectsConcrete.building.floor.elevator;

import drawable.drawableBase.creatureWithTexture.DrawableCreature;
import model.GuiModel;
import model.objects.movingObject.Creature;
import common.tools.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;

/*
 * Spaces behind elevators
 */
public class ElevatorBlackSpace extends DrawableCreature {
    private final Color BLACK_SPACES_COLOR;

    public ElevatorBlackSpace(Vector2D position, Creature parentElevator, GuiModel guiModel) {
        super(position, parentElevator.getSize());
        size = new Point(size.x + guiModel.DRAW_SETTINGS.ELEVATOR_BORDER_THICKNESS * 2, size.y + guiModel.DRAW_SETTINGS.ELEVATOR_BORDER_THICKNESS);
        this.BLACK_SPACES_COLOR = guiModel.COLOR_SETTINGS.GUI_BACK_GROUND_COLOR;
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 2;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(BLACK_SPACES_COLOR);
        gameDrawer.drawFilledRect(this);
    }
}


