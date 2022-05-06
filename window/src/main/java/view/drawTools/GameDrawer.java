package view.drawTools;

import drawable.drawableBase.Drawable;
import drawable.drawableBase.drawableWithTexture.DrawableWithTexture;
import lombok.RequiredArgsConstructor;
import tools.Vector2D;

import java.awt.*;

/*
 * this class transform game coordinates to normal coordinates and viceversa , so u can change
 * real coordinates and have an image streched in a normal natural way.
 */
@RequiredArgsConstructor
public class GameDrawer {
    // The ratio of game coordinates to real
    private final GameScaler gameScaler;
    private Graphics2D graphics2D;

    public void setColor(Color red) {
        graphics2D.setColor(red);
    }

    public void drawFilledRect(Vector2D position, Vector2D size) {
        graphics2D.fillRect(
                (int) gameScaler.getFromGameToRealCoordinate(position, size.y).x,
                (int) gameScaler.getFromGameToRealCoordinate(position, size.y).y,
                (int) gameScaler.getFromRealToGameLength(size.x),
                (int) gameScaler.getFromRealToGameLength(size.y)
        );
    }

    public void drawFilledRect(Vector2D position, Vector2D size, Color borderColor, int thickness) {
        drawFilledRect(position, size);
        Stroke oldStroke = graphics2D.getStroke();
        graphics2D.setColor(borderColor);
        graphics2D.setStroke(new BasicStroke((float) gameScaler.getFromRealToGameLength(thickness)));
        graphics2D.drawRect(
                (int) gameScaler.getFromGameToRealCoordinate(position, size.y).x,
                (int) gameScaler.getFromGameToRealCoordinate(position, size.y).y,
                (int) gameScaler.getFromRealToGameLength(size.x),
                (int) gameScaler.getFromRealToGameLength(size.y)
        );
        graphics2D.setStroke(oldStroke);
    }


    public void setFont(String fontName, int type, int size) {
        graphics2D.setFont(new Font(fontName, type, (int) gameScaler.getFromRealToGameLength(size)));
    }

    public void draw(String text, Vector2D position) {
        graphics2D.drawString(text,
                (int) gameScaler.getFromGameToRealCoordinate(position, 0).x,
                (int) gameScaler.getFromGameToRealCoordinate(position, 0).y);
    }

    public void drawFilledRect(Drawable drawableCreature) {
        Vector2D positionOfTheCreature = getPositionCenteredBySettings(drawableCreature);
        graphics2D.fillRect(
                (int) gameScaler.getFromGameToRealCoordinate(positionOfTheCreature, drawableCreature.getSize().y).x,
                (int) gameScaler.getFromGameToRealCoordinate(positionOfTheCreature, drawableCreature.getSize().y).y,
                (int) gameScaler.getFromRealToGameLength(drawableCreature.getSize().x),
                (int) gameScaler.getFromRealToGameLength(drawableCreature.getSize().y)
        );
    }

    public void drawRect(Vector2D position, Vector2D size, double thickness) {
        Stroke oldStroke = graphics2D.getStroke();
        graphics2D.setStroke(new BasicStroke((float) gameScaler.getFromRealToGameLength(thickness)));

        Vector2D positionOfTheRect = position.getSubbed(new Vector2D(size.x / 2, 0));
        graphics2D.drawRect(
                (int) gameScaler.getFromGameToRealCoordinate(positionOfTheRect, size.y).x,
                (int) gameScaler.getFromGameToRealCoordinate(positionOfTheRect, size.y).y,
                (int) gameScaler.getFromRealToGameLength(size.x),
                (int) gameScaler.getFromRealToGameLength(size.y));

        graphics2D.setStroke(oldStroke);
    }

    public void startDraw(Graphics g) {
        graphics2D = (Graphics2D) g;
    }

    public void draw(DrawableWithTexture drawableWithTexture) {
        Vector2D positionOfTheCreature = getPositionCenteredBySettings(drawableWithTexture);

        graphics2D.drawImage(drawableWithTexture.getImage(),
                (int) gameScaler.getFromGameToRealCoordinate(positionOfTheCreature, drawableWithTexture.getSize().y).x,
                (int) gameScaler.getFromGameToRealCoordinate(positionOfTheCreature, drawableWithTexture.getSize().y).y,
                (int) gameScaler.getFromRealToGameLength(drawableWithTexture.getSize().x),
                (int) gameScaler.getFromRealToGameLength(drawableWithTexture.getSize().y),
                null);
    }

    private Vector2D getPositionCenteredBySettings(Drawable drawable) {
        return switch (drawable.getDrawCenter()) {
            case CENTER_BY_X -> drawable.getPosition().getSubbed(
                    new Vector2D(drawable.getSize().x / 2., 0));
            case CENTER_BY_Y -> drawable.getPosition().getSubbed(
                    new Vector2D(0, drawable.getSize().y / 2.));
            case CENTER_BY_XY -> drawable.getPosition().getSubbed(
                    new Vector2D(drawable.getSize().x / 2., drawable.getSize().y / 2.));
        };
    }
}
