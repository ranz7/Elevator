package view;

import model.objects.movingObject.Creature;
import tools.Vector2D;

import java.awt.*;
/*
* this class transforms game coordinates to normal coordinates and viceversa , so u can change
* real coordinates and have an image streched in a normal natural way.
 */
public class GameDrawer {
    // The ratio of game coordinates to real
    private final Graphics2D GRAPHICS_2D;
    private final Point REAL_SIZE;
    private final double SCALING_COEFFICIENT;
    private final Point originalOffset;

    public GameDrawer(Point gameSize, Point realSize, Graphics2D g2d) {
        this.GRAPHICS_2D = g2d;
        this.REAL_SIZE = realSize;
        SCALING_COEFFICIENT = Math.max(((double) gameSize.x) / REAL_SIZE.x,
                ((double) gameSize.y) / REAL_SIZE.y);
        Point new_size = new Point((int) (((double) gameSize.x) / SCALING_COEFFICIENT),
                (int) (((double) gameSize.y) / SCALING_COEFFICIENT));

        originalOffset = new Point((REAL_SIZE.x - new_size.x) / 2, (REAL_SIZE.y - new_size.y) / 2);
    }

    public void setColor(Color red) {
        GRAPHICS_2D.setColor(red);
    }

    public void fillRect(Vector2D position, Point size) {
        GRAPHICS_2D.fillRect(
                (int) (originalOffset.x + (position.x) / SCALING_COEFFICIENT),
                (int) (REAL_SIZE.y - originalOffset.y - (position.y + size.y) / SCALING_COEFFICIENT),
                (int) (size.x / SCALING_COEFFICIENT),
                (int) (size.y / SCALING_COEFFICIENT)
        );
    }
    public void fillRect(Vector2D position, Point size, Color borderColor, int thickness) {
        fillRect(position,size);
        Stroke oldStroke = GRAPHICS_2D.getStroke();
        GRAPHICS_2D.setColor(borderColor);
        GRAPHICS_2D.setStroke(new BasicStroke((float) (thickness / SCALING_COEFFICIENT)));
        GRAPHICS_2D.drawRect(
                (int) (originalOffset.x + (position.x) / SCALING_COEFFICIENT),
                (int) (REAL_SIZE.y - originalOffset.y - (position.y + size.y) / SCALING_COEFFICIENT),
                (int) (size.x / SCALING_COEFFICIENT),
                (int) (size.y / SCALING_COEFFICIENT)
        );
        GRAPHICS_2D.setStroke(oldStroke);
    }

    public void fillRect(Creature creature) {
        GRAPHICS_2D.fillRect(
                (int) (originalOffset.x + (creature.getPosition().x - creature.getSize().x / 2) / SCALING_COEFFICIENT),
                (int) (REAL_SIZE.y - originalOffset.y -
                        (creature.getSize().y + creature.getPosition().y) / SCALING_COEFFICIENT),
                (int) (creature.getSize().x / SCALING_COEFFICIENT),
                (int) (creature.getSize().y / SCALING_COEFFICIENT)
        );
    }

    public void drawRect(Vector2D position, Point size, double thickness) {
        Stroke oldStroke = GRAPHICS_2D.getStroke();
        GRAPHICS_2D.setStroke(new BasicStroke((float) (thickness / SCALING_COEFFICIENT)));

        GRAPHICS_2D.drawRect(
                (int) (originalOffset.x + (position.x - size.x / 2) / SCALING_COEFFICIENT),
                (int) (REAL_SIZE.y - originalOffset.y - (position.y + size.y) / SCALING_COEFFICIENT),
                (int) (size.x / SCALING_COEFFICIENT),
                (int) (size.y / SCALING_COEFFICIENT)
        );

        GRAPHICS_2D.setStroke(oldStroke);

    }
}
