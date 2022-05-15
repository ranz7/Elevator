package view.graphics;

import lombok.RequiredArgsConstructor;
import tools.Vector2D;

import java.awt.*;

/*
 * this class transform game coordinates to normal coordinates and viceversa , so u can change
 * real coordinates and have an image streched in a normal natural way.
 */
@RequiredArgsConstructor
public class GameGraphics {
    // The ratio of game coordinates to real
    protected final GameScaler gameScaler;
    protected Graphics2D graphics2D;

    public void setColor(Color red) {
        graphics2D.setColor(red);
    }

    private void fillRect(Vector2D position, Vector2D size, Color fillColor) {
        graphics2D.setColor(fillColor);
        var realPosition = gameScaler.getFromGameToRealCoordinate(position, size.y);
        graphics2D.fillRect(
                (int) realPosition.x,
                (int) realPosition.y,
                (int) gameScaler.getFromGameToRealLength(size.x),
                (int) gameScaler.getFromGameToRealLength(size.y)
        );
    }

    public void drawFilledRect(Vector2D position, Vector2D size,
                               Color fillColor,
                               Color borderColor, double thickness) {
        fillRect(position, size, fillColor);
        drawOnlyBorderRect(position, size, borderColor, thickness);
    }

    public void drawOnlyBorderRect(Vector2D position, Vector2D size, Color borderColor, double thickness) {
        Stroke oldStroke = graphics2D.getStroke();
        graphics2D.setColor(borderColor);
        var newThickness = (float) gameScaler.getFromGameToRealLength(thickness);
        graphics2D.setStroke(new BasicStroke(newThickness));
        var realPosition = gameScaler.getFromGameToRealCoordinate(position, size.y);
        graphics2D.drawRect(
                (int) realPosition.x,
                (int) realPosition.y,
                (int) gameScaler.getFromGameToRealLength(size.x),
                (int) gameScaler.getFromGameToRealLength(size.y)
        );
        graphics2D.setStroke(oldStroke);
    }

    public void drawText(String text, Vector2D position, double size, Color color) {
        int fontSize = (int) gameScaler.getFromGameToRealLength(size);
        setColor(color);
        var font = new Font("TimesRoman", Font.PLAIN, fontSize);
        graphics2D.setFont(font);
        int metrics = graphics2D.getFontMetrics(font).stringWidth(text);
        graphics2D.drawString(text,
                (int) gameScaler.getFromGameToRealCoordinate(position, 0).x - metrics / 2,
                (int) gameScaler.getFromGameToRealCoordinate(position, 0).y);

    }

    public void drawImage(Image image, Vector2D position, Vector2D size) {
        var realPosition = gameScaler.getFromGameToRealCoordinate(position, size.y);
        graphics2D.drawImage(image,
                (int) realPosition.x,
                (int) realPosition.y,
                (int) gameScaler.getFromGameToRealLength(size.x),
                (int) gameScaler.getFromGameToRealLength(size.y),
                null);
    }

    public void prepareDrawer(Graphics g) {
        graphics2D = (Graphics2D) g;
    }
}
