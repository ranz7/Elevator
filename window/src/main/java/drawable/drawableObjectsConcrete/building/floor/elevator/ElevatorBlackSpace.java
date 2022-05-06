package drawable.drawableObjectsConcrete.building.floor.elevator;

import drawable.drawableBase.Drawable;
import model.GuiModel;
import model.objects.Creature;
import tools.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;

/*
 * Spaces behind elevators
 */
public class ElevatorBlackSpace extends Drawable {
    private final Color BLACK_SPACES_COLOR;

    public ElevatorBlackSpace(Vector2D position, Creature parentElevator, GuiModel guiModel) {
        super(position, parentElevator.getSize());
        size = new Vector2D(size.x + guiModel.drawSettings.ELEVATOR_BORDER_THICKNESS * 2, size.y + guiModel.drawSettings.ELEVATOR_BORDER_THICKNESS);
        this.BLACK_SPACES_COLOR = guiModel.colorSettings.windowBackGround;
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


