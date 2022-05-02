package drawable.drawableObjects.elevator;

import model.objects.movingObject.Creature;
import drawable.Drawable;
import tools.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;

/*
 * Spaces behind elevators
 */
public class BlackSpace extends Creature implements Drawable {
    private final Color BLACK_SPACES_COLOR;

    public BlackSpace(Vector2D position, Creature parentElevator, Color blackSpacesColor, int borderSize) {
        super(position, parentElevator.getSize());
        size = new Point(size.x + borderSize * 2, size.y + borderSize);
        this.BLACK_SPACES_COLOR = blackSpacesColor;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(BLACK_SPACES_COLOR);
        gameDrawer.fillRect(this);
    }

    public void tick(long delta_time) {
    }
}

