package view.drawTools;

import lombok.RequiredArgsConstructor;
import model.objects.movingObject.Creature;
import tools.Vector2D;
import view.drawTools.GameScaler;

import java.awt.*;

/*
 * this class transform game coordinates to normal coordinates and viceversa , so u can change
 * real coordinates and have an image streched in a normal natural way.
 */
@RequiredArgsConstructor
public class GameDrawer {
    // The ratio of game coordinates to real
    private final GameScaler game;
    private Graphics2D graphics2D;

    public void setColor(Color red) {
        graphics2D.setColor(red);
    }

    public void fillRect(Vector2D position, Point size) {
        graphics2D.fillRect(
                (int) game.getFromGameToRealCoordinate(position, size.y).x,
                (int) game.getFromGameToRealCoordinate(position, size.y).y,
                (int) game.getFromRealToGameLength(size.x),
                (int) game.getFromRealToGameLength(size.y)
        );
    }

    public void fillRect(Vector2D position, Point size, Color borderColor, int thickness) {
        fillRect(position, size);
        Stroke oldStroke = graphics2D.getStroke();
        graphics2D.setColor(borderColor);
        graphics2D.setStroke(new BasicStroke((float) game.getFromRealToGameLength(thickness)));
        graphics2D.drawRect(
                (int) game.getFromGameToRealCoordinate(position, size.y).x,
                (int) game.getFromGameToRealCoordinate(position, size.y).y,
                (int) game.getFromRealToGameLength(size.x),
                (int) game.getFromRealToGameLength(size.y)
        );
        graphics2D.setStroke(oldStroke);
    }


    public void setFont(String fontName, int type, int size) {
        graphics2D.setFont(new Font(fontName, type, (int) game.getFromRealToGameLength(size)));
    }

    public void drawString(String text, Vector2D position) {
        graphics2D.drawString(text,
                (int) game.getFromGameToRealCoordinate(position, 0).x,
                (int) game.getFromGameToRealCoordinate(position, 0).y);
    }

    public void fillRect(Creature creature) {
        Vector2D positionOfTheCreature = creature.getPosition().getSubbed(new Vector2D(creature.getSize().x / 2, 0));
        graphics2D.fillRect(
                (int) game.getFromGameToRealCoordinate(positionOfTheCreature, creature.getSize().y).x,
                (int) game.getFromGameToRealCoordinate(positionOfTheCreature, creature.getSize().y).y,
                (int) game.getFromRealToGameLength(creature.getSize().x),
                (int) game.getFromRealToGameLength(creature.getSize().y)
        );
    }

    public void drawRect(Vector2D position, Point size, double thickness) {
        Stroke oldStroke = graphics2D.getStroke();
        graphics2D.setStroke(new BasicStroke((float) game.getFromRealToGameLength(thickness)));

        Vector2D positionOfTheRect = position.getSubbed(new Vector2D(size.x / 2, 0));
        graphics2D.drawRect(
                (int) game.getFromGameToRealCoordinate(positionOfTheRect, size.y).x,
                (int) game.getFromGameToRealCoordinate(positionOfTheRect, size.y).y,
                (int) game.getFromRealToGameLength(size.x),
                (int) game.getFromRealToGameLength(size.y));

        graphics2D.setStroke(oldStroke);
    }

    public void startDraw(Graphics g) {
        graphics2D = (Graphics2D) g;
    }

}
