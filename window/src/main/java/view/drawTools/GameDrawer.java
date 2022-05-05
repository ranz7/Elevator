package view.drawTools;

import drawable.drawableBase.creatureWithTexture.DrawableCreature;
import drawable.drawableBase.creatureWithTexture.DrawableCreatureWithTexture;
import lombok.RequiredArgsConstructor;
import common.tools.Vector2D;

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

    public void drawFilledRect(Vector2D position, Point size) {
        graphics2D.fillRect(
                (int) game.getFromGameToRealCoordinate(position, size.y).x,
                (int) game.getFromGameToRealCoordinate(position, size.y).y,
                (int) game.getFromRealToGameLength(size.x),
                (int) game.getFromRealToGameLength(size.y)
        );
    }

    public void drawFilledRect(Vector2D position, Point size, Color borderColor, int thickness) {
        drawFilledRect(position, size);
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

    public void draw(String text, Vector2D position) {
        graphics2D.drawString(text,
                (int) game.getFromGameToRealCoordinate(position, 0).x,
                (int) game.getFromGameToRealCoordinate(position, 0).y);
    }

    public void drawFilledRect(DrawableCreature drawableCreature) {
        Vector2D positionOfTheCreature = getPositionCenteredBySettings(drawableCreature);
        graphics2D.fillRect(
                (int) game.getFromGameToRealCoordinate(positionOfTheCreature, drawableCreature.getSize().y).x,
                (int) game.getFromGameToRealCoordinate(positionOfTheCreature, drawableCreature.getSize().y).y,
                (int) game.getFromRealToGameLength(drawableCreature.getSize().x),
                (int) game.getFromRealToGameLength(drawableCreature.getSize().y)
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

    public void draw(DrawableCreatureWithTexture drawableCreature) {
        Vector2D positionOfTheCreature = getPositionCenteredBySettings(drawableCreature);

        graphics2D.drawImage(drawableCreature.getImage(),
                (int) game.getFromGameToRealCoordinate(positionOfTheCreature, drawableCreature.getSize().y).x,
                (int) game.getFromGameToRealCoordinate(positionOfTheCreature, drawableCreature.getSize().y).y,
                (int) game.getFromRealToGameLength(drawableCreature.getSize().x),
                (int) game.getFromRealToGameLength(drawableCreature.getSize().y),
                null);
    }

    private Vector2D getPositionCenteredBySettings(DrawableCreature drawableCreature) {
        return switch (drawableCreature.getDrawCenter()) {
            case CENTER_BY_X -> drawableCreature.getPosition().getSubbed(
                    new Vector2D(drawableCreature.getSize().x / 2., 0));
            case CENTER_BY_Y -> drawableCreature.getPosition().getSubbed(
                    new Vector2D(0, drawableCreature.getSize().y / 2.));
            case CENTER_BY_XY -> drawableCreature.getPosition().getSubbed(
                    new Vector2D(drawableCreature.getSize().x / 2., drawableCreature.getSize().y / 2.));
        };
    }
}
