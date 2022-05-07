package view.drawTools.drawer;

import lombok.RequiredArgsConstructor;
import tools.Vector2D;
import view.drawTools.scaler.GameScaler;

import java.awt.*;

@RequiredArgsConstructor
public class ShapeDrawer {
    // The ratio of game coordinates to real
    protected final GameScaler gameScaler;
    protected Graphics2D graphics2D;

    public void setColor(Color red) {
        graphics2D.setColor(red);
    }

    public void fillRect(Vector2D position, Vector2D size) {
        graphics2D.fillRect(
                (int) gameScaler.getFromGameToRealCoordinate(position, size.y).x,
                (int) gameScaler.getFromGameToRealCoordinate(position, size.y).y,
                (int) gameScaler.getFromRealToGameLength(size.x),
                (int) gameScaler.getFromRealToGameLength(size.y)
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

    public void drawRect(Vector2D position, Vector2D size, Color borderColor, double thickness) {
        fillRect(position, size);
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

    public void startDraw(Graphics g) {
        graphics2D = (Graphics2D) g;
    }

    public void drawText(String text, Vector2D position, double size, Color color) {
        int fontSize = (int) gameScaler.getFromRealToGameLength(size);
        setColor(color);
        graphics2D.setFont(
                new Font("TimesRoman", Font.PLAIN, fontSize));
        graphics2D.drawString(text,
                (int) gameScaler.getFromGameToRealCoordinate(position, fontSize).x,
                (int) gameScaler.getFromGameToRealCoordinate(position, fontSize).y);

    }
}
