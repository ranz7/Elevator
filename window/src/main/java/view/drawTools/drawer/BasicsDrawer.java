package view.drawTools.drawer;

import lombok.RequiredArgsConstructor;
import tools.Vector2D;
import view.drawTools.scaler.GameScaler;

import java.awt.*;

@RequiredArgsConstructor
public class BasicsDrawer {
    // The ratio of game coordinates to real
    protected final GameScaler gameScaler;
    protected Graphics2D graphics2D;

    public void setColor(Color red) {
        graphics2D.setColor(red);
    }

    private void fillRect(Vector2D position, Vector2D size, Color fillColor) {
        graphics2D.setColor(fillColor);
        graphics2D.fillRect(
                (int) gameScaler.getFromGameToRealCoordinate(position, size.y).x,
                (int) gameScaler.getFromGameToRealCoordinate(position, size.y).y,
                (int) gameScaler.getFromGameToRealLength(size.x),
                (int) gameScaler.getFromGameToRealLength(size.y)
        );
    }

    protected void drawFilledRect(Vector2D position, Vector2D size,
                               Color fillColor,
                               Color borderColor, double thickness) {
        fillRect(position, size, fillColor);
        drawOnlyBorderRect(position, size, borderColor, thickness);
    }

    protected void drawOnlyBorderRect(Vector2D position, Vector2D size, Color borderColor, double thickness) {
        Stroke oldStroke = graphics2D.getStroke();
        graphics2D.setColor(borderColor);
        graphics2D.setStroke(new BasicStroke((float) gameScaler.getFromGameToRealLength(thickness)));
        graphics2D.drawRect(
                (int) gameScaler.getFromGameToRealCoordinate(position, size.y).x,
                (int) gameScaler.getFromGameToRealCoordinate(position, size.y).y,
                (int) gameScaler.getFromGameToRealLength(size.x),
                (int) gameScaler.getFromGameToRealLength(size.y)
        );
        graphics2D.setStroke(oldStroke);
    }

    protected void drawText(String text, Vector2D position, double size, Color color) {
        int fontSize = (int) gameScaler.getFromGameToRealLength(size);
        setColor(color);
        graphics2D.setFont(
                new Font("TimesRoman", Font.PLAIN, fontSize));
        graphics2D.drawString(text,
                (int) gameScaler.getFromGameToRealCoordinate(position, fontSize).x,
                (int) gameScaler.getFromGameToRealCoordinate(position, fontSize).y);

    }
    protected void drawImage(Image image,Vector2D position, Vector2D size) {
        graphics2D.drawImage(image,
                (int) gameScaler.getFromGameToRealCoordinate(position, size.y).x,
                (int) gameScaler.getFromGameToRealCoordinate(position, size.y).y,
                (int) gameScaler.getFromGameToRealLength(size.x),
                (int) gameScaler.getFromGameToRealLength(size.y),
                null);
    }

    public void prepareDrawer(Graphics g) {
        graphics2D = (Graphics2D) g;
    }
}
