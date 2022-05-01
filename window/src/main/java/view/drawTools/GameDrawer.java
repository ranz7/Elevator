package view.drawTools;

import lombok.RequiredArgsConstructor;
import model.objects.movingObject.Creature;
import tools.Vector2D;

import java.awt.*;

/*
 * this class transform game coordinates to normal coordinates and viceversa , so u can change
 * real coordinates and have an image streched in a normal natural way.
 */
@RequiredArgsConstructor
public class GameDrawer {
    // The ratio of game coordinates to real
    final GameScaler GAME_SCALER;
    private Graphics2D graphics2D;

    public void setColor(Color red) {
        graphics2D.setColor(red);
    }

    public void fillRect(Vector2D position, Point size) {
        graphics2D.fillRect(
                (int) GAME_SCALER.getFromRealToGameCoordinate(position, size.y).x,
                (int) GAME_SCALER.getFromRealToGameCoordinate(position, size.y).y,
                (int) GAME_SCALER.getFromRealToGameLength(size.x),
                (int) GAME_SCALER.getFromRealToGameLength(size.y)
        );
    }

    public void fillRect(Vector2D position, Point size, Color borderColor, int thickness) {
        fillRect(position, size);
        Stroke oldStroke = graphics2D.getStroke();
        graphics2D.setColor(borderColor);
        graphics2D.setStroke(new BasicStroke((float) GAME_SCALER.getFromRealToGameLength(thickness)));
        graphics2D.drawRect(
                (int) GAME_SCALER.getFromRealToGameCoordinate(position, size.y).x,
                (int) GAME_SCALER.getFromRealToGameCoordinate(position, size.y).y,
                (int) GAME_SCALER.getFromRealToGameLength(size.x),
                (int) GAME_SCALER.getFromRealToGameLength(size.y)
        );
        graphics2D.setStroke(oldStroke);
    }


    public void setFont(String fontName, int type, int size) {
        graphics2D.setFont(new Font(fontName, type, (int) GAME_SCALER.getFromRealToGameLength(size)));
    }

    public void drawString(String text, Vector2D position) {
        graphics2D.drawString(text,
                (int) GAME_SCALER.getFromRealToGameCoordinate(position, 0).x,
                (int) GAME_SCALER.getFromRealToGameCoordinate(position, 0).y);
    }

    public void fillRect(Creature creature) {
        Vector2D positionOfTheCreature = creature.getPosition().getSubbed(new Vector2D(creature.getSize().x / 2, 0));
        graphics2D.fillRect(
                (int) GAME_SCALER.getFromRealToGameCoordinate(positionOfTheCreature, creature.getSize().y).x,
                (int) GAME_SCALER.getFromRealToGameCoordinate(positionOfTheCreature, creature.getSize().y).y,
                (int) GAME_SCALER.getFromRealToGameLength(creature.getSize().x),
                (int) GAME_SCALER.getFromRealToGameLength(creature.getSize().y)
        );
    }

    public void drawRect(Vector2D position, Point size, double thickness) {
        Stroke oldStroke = graphics2D.getStroke();
        graphics2D.setStroke(new BasicStroke((float) GAME_SCALER.getFromRealToGameLength(thickness)));

        Vector2D positionOfTheRect = position.getSubbed(new Vector2D(size.x / 2, 0));
        graphics2D.drawRect(
                (int) GAME_SCALER.getFromRealToGameCoordinate(positionOfTheRect, size.y).x,
                (int) GAME_SCALER.getFromRealToGameCoordinate(positionOfTheRect, size.y).y,
                (int) GAME_SCALER.getFromRealToGameLength(size.x),
                (int) GAME_SCALER.getFromRealToGameLength(size.y));

        graphics2D.setStroke(oldStroke);
    }

    public void startDraw(Graphics g) {
        graphics2D = (Graphics2D) g;
    }

}
