package drawable.drawableObjects.building.floor;

import drawable.ColorSettings;
import model.WindowModel;
import model.objects.movingObject.Creature;
import drawable.Drawable;
import tools.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/*
 * Spaces behind elevators
 */
public class ElevatorBlackSpace extends Creature implements Drawable {
    private final Color BLACK_SPACES_COLOR;

    public ElevatorBlackSpace(Vector2D position, Creature parentElevator, WindowModel windowModel) {
        super(position, parentElevator.getSize());
        size = new Point(size.x + windowModel.DRAW_SETTINGS.BORDER_SIZE * 2, size.y + windowModel.DRAW_SETTINGS.BORDER_SIZE);
        this.BLACK_SPACES_COLOR = windowModel.COLOR_SETTINGS.BLACK_SPACE_COLOR;
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 2;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(BLACK_SPACES_COLOR);
        gameDrawer.fillRect(this);
    }

    public void tick(long delta_time) {
    }

    @Override
    public List<Drawable> getDrawables() {
        var drawables = new LinkedList<Drawable>();
        drawables.add(this);
        return drawables;
    }
}


