package drawable.drawableObjects;

import drawable.Drawable;
import model.objects.movingObject.Creature;
import tools.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;

public class ElevatorBorder extends Creature implements Drawable {
    public final int BORDER_SIZE = 2;

    private final Creature PARENT_ELEVATOR;
    private final int WALL_SIZE;
    private final Color BORDER_COLOR;
    private final Color NUMBER_COLOR;

    public ElevatorBorder(Vector2D position, Creature parentElevator, int WALL_SIZE, Color borderColor,
                          Color numberColor) {
        super(position, parentElevator.getSize());
        size = new Point(size.x + BORDER_SIZE * 2, size.y + BORDER_SIZE);
        PARENT_ELEVATOR = parentElevator;
        this.WALL_SIZE = WALL_SIZE;

        BORDER_COLOR = borderColor;
        NUMBER_COLOR = numberColor;
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
        gameDrawer.drawString(
                ((int) (PARENT_ELEVATOR.getPosition().y / WALL_SIZE + 1)) + "", positionOfText.getAdded(
                        new Vector2D(-2, size.y - BORDER_SIZE + 3)));
    }

    public void tick(long delta_time) {
    }

}
