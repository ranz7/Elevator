package view;

import model.objects.movingObject.Creature;
import tools.Vector2D;

import java.awt.*;

/*
 * this class transform game coordinates to normal coordinates and viceversa , so u can change
 * real coordinates and have an image streched in a normal natural way.
 */
public class GameDrawer {
    // The ratio of game coordinates to real
    private Graphics2D graphics2D;
    private Dimension screenSizeAfterShift;
    private double scalingCoefficient;
    private Point drawOffset;

    public void setColor(Color red) {
        graphics2D.setColor(red);
    }

    public void fillRect(Vector2D position, Point size) {
        graphics2D.fillRect(
                (int) (drawOffset.x + (position.x) / scalingCoefficient),
                (int) (screenSizeAfterShift.height - drawOffset.y - (position.y + size.y) / scalingCoefficient),
                (int) (size.x / scalingCoefficient),
                (int) (size.y / scalingCoefficient)
        );
    }

    public void fillRect(Vector2D position, Point size, Color borderColor, int thickness) {
        fillRect(position, size);
        Stroke oldStroke = graphics2D.getStroke();
        graphics2D.setColor(borderColor);
        graphics2D.setStroke(new BasicStroke((float) (thickness / scalingCoefficient)));
        graphics2D.drawRect(
                (int) (drawOffset.x + (position.x) / scalingCoefficient),
                (int) (screenSizeAfterShift.height - drawOffset.y - (position.y + size.y) / scalingCoefficient),
                (int) (size.x / scalingCoefficient),
                (int) (size.y / scalingCoefficient)
        );
        graphics2D.setStroke(oldStroke);
    }

    public void setFont(String fontName, int type, int size) {
        graphics2D.setFont(new Font(fontName, type, (int) (size / scalingCoefficient)));
    }

    public void drawString(String text, Vector2D position) {
        graphics2D.drawString(text, (int) (position.x / scalingCoefficient + drawOffset.x),
                (int) (screenSizeAfterShift.height - position.y / scalingCoefficient - drawOffset.y));
    }

    public void fillRect(Creature creature) {
        graphics2D.fillRect(
                (int) (drawOffset.x + (creature.getPosition().x - creature.getSize().x / 2) / scalingCoefficient),
                (int) (screenSizeAfterShift.height - drawOffset.y -
                        (creature.getSize().y + creature.getPosition().y) / scalingCoefficient),
                (int) (creature.getSize().x / scalingCoefficient),
                (int) (creature.getSize().y / scalingCoefficient)
        );
    }

    public void drawRect(Vector2D position, Point size, double thickness) {
        Stroke oldStroke = graphics2D.getStroke();
        graphics2D.setStroke(new BasicStroke((float) (thickness / scalingCoefficient)));

        graphics2D.drawRect(
                (int) (drawOffset.x + (position.x - size.x / 2) / scalingCoefficient),
                (int) (screenSizeAfterShift.height - drawOffset.y - (position.y + size.y) / scalingCoefficient),
                (int) (size.x / scalingCoefficient),
                (int) (size.y / scalingCoefficient)
        );

        graphics2D.setStroke(oldStroke);

    }

    public void updateGameDrawerWithSizes(Dimension screenSize, Point buildingSize, Graphics g) {
        var blackZone = new Point(100, 100);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(blackZone.x / 2, blackZone.y / 2);
        graphics2D = g2d;

        screenSizeAfterShift = new Dimension(
                screenSize.width - blackZone.x,
                screenSize.height - blackZone.y);
        scalingCoefficient = Math.max(((double) buildingSize.x) / screenSizeAfterShift.width,
                ((double) buildingSize.y) / screenSizeAfterShift.height);

        Point sizeOfBuildingAfterRescale = new Point((int) (((double) buildingSize.x) / scalingCoefficient),
                (int) (((double) buildingSize.y) / scalingCoefficient));

        drawOffset = new Point((screenSizeAfterShift.width - sizeOfBuildingAfterRescale.x) / 2,
                (screenSizeAfterShift.height - sizeOfBuildingAfterRescale.y) / 2);
    }

    public void restore() {
        var blackZone = new Point(100, 100);
        graphics2D.translate(-blackZone.x / 2, -blackZone.y / 2);
    }
}
