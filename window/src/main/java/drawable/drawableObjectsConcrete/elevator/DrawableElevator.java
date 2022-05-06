package drawable.drawableObjectsConcrete.elevator;

import configs.CanvasSettings.MainSettings;
import drawable.drawableBase.Drawable;
import model.objects.Creature;
import tools.Vector2D;
import view.drawTools.GameDrawer;
import lombok.Getter;

import java.awt.*;
import java.util.List;

@Getter
public class DrawableElevator extends Drawable {
    public final Color BACK_GROUND_COLOR;
    public final ElevatorDoors DOORS;

    public DrawableElevator(Creature creature, MainSettings settings) {
        super(creature);
        BACK_GROUND_COLOR = settings.elevatorBackGroundColor();
        DOORS = new ElevatorDoors(this, settings);
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 4;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (!isVisible()) {
            return;
        }
        if (DOORS.isClosed()) {
            return;
        }
        gameDrawer.setColor(BACK_GROUND_COLOR);
        gameDrawer.drawFilledRect(position.getSubbed(new Vector2D(this.size.x / 2., 0)), this.size);
    }

    @Override
    public List<Drawable> getDrawables() {
        List<Drawable> drawables = super.getDrawables();
        drawables.add(DOORS);
        return drawables;
    }
}
