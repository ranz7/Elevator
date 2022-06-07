package model.planes.graphics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tools.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

/*
 * this class transform game coordinates to normal coordinates and viceversa , so u can change
 * real coordinates and have an image streched in a normal natural way.
 */
@RequiredArgsConstructor
public class Painter {
    // The ratio of game coordinates to real
    @Getter
    private final Scaler scaler;
    protected Graphics2D graphics2D;

    public void setColor(Color red) {
        graphics2D.setColor(red);
    }

    private void fillRect(Vector2D position, Vector2D size, Color fillColor) {
        graphics2D.setColor(fillColor);
        var realPosition = scaler.getFromGameToRealCoordinate(position, size.y);
        graphics2D.fillRect(
                (int) realPosition.x,
                (int) realPosition.y,
                (int) scaler.getFromGameToRealLength(size.x),
                (int) scaler.getFromGameToRealLength(size.y)
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
        var newThickness = (float) scaler.getFromGameToRealLength(thickness);
        graphics2D.setStroke(new BasicStroke(newThickness));
        var realPosition = scaler.getFromGameToRealCoordinate(position, size.y);
        graphics2D.drawRect(
                (int) realPosition.x,
                (int) realPosition.y,
                (int) scaler.getFromGameToRealLength(size.x),
                (int) scaler.getFromGameToRealLength(size.y)
        );
        graphics2D.setStroke(oldStroke);
    }

    public void drawText(String text, Vector2D position, Vector2D size, Color color) {
         int fontSize = (int) scaler.getFromGameToRealLength(size.y);
        setColor(color);
        var font = new Font("TimesRoman", Font.PLAIN, fontSize);
        graphics2D.setFont(font);
        int width = (int) graphics2D.getFontMetrics(font).stringWidth(text);
        graphics2D.drawString(text,
                (int) scaler.getFromGameToRealCoordinate(position.addByX(size.x / 2), 1).x - width / 2,
                (int) scaler.getFromGameToRealCoordinate(position.addByX(size.x / 2), 1).y);

    }

    public void drawImage(Image image, Vector2D position, Vector2D size) {
        var realPosition = scaler.getFromGameToRealCoordinate(position, size.y);
        graphics2D.drawImage(image,
                (int) realPosition.x,
                (int) realPosition.y,
                (int) scaler.getFromGameToRealLength(size.x),
                (int) scaler.getFromGameToRealLength(size.y),
                null);
    }

    public void prepareDrawer(Graphics g) {
        graphics2D = (Graphics2D) g;
    }

    public void drawFilledEllipse(Vector2D position, Vector2D size, Color fillColor,
                                  Color borderColor, int thickness) {
        fillEllipse(position, size, fillColor);
        drawOnlyBorderEllipse(position, size, borderColor, thickness);
    }

    private void drawOnlyBorderEllipse(Vector2D position, Vector2D size, Color borderColor, int thickness) {
        Stroke oldStroke = graphics2D.getStroke();
        graphics2D.setColor(borderColor);
        var newThickness = (float) scaler.getFromGameToRealLength(thickness);
        graphics2D.setStroke(new BasicStroke(newThickness));
        var realPosition = scaler.getFromGameToRealCoordinate(position, size.y);
        graphics2D.drawOval(
                (int) realPosition.x,
                (int) realPosition.y,
                (int) scaler.getFromGameToRealLength(size.x),
                (int) scaler.getFromGameToRealLength(size.y)
        );
        graphics2D.setStroke(oldStroke);
    }

    private void fillEllipse(Vector2D position, Vector2D size, Color fillColor) {
        graphics2D.setColor(fillColor);
        var realPosition = scaler.getFromGameToRealCoordinate(position, size.y);
        graphics2D.fillOval(
                (int) realPosition.x,
                (int) realPosition.y,
                (int) scaler.getFromGameToRealLength(size.x),
                (int) scaler.getFromGameToRealLength(size.y)
        );
    }

    public void drawBlackSpaces() {
        var leftTop = scaler.getFromGameToRealCoordinate(new Vector2D(0, 0), 0);
        Vector2D gameSize = scaler.getGameSize();
        var rightTop = scaler.getFromGameToRealCoordinate(new Vector2D(gameSize.x, 0), 0);
        var rightBottom = scaler.getFromGameToRealCoordinate(new Vector2D(gameSize.x, gameSize.y), 0);

/*
        graphics2D.setColor(new Color(255, 184, 0));
        graphics2D.fillRect(0, 0, (int) rightBottom.x, (int) rightBottom.y);
        graphics2D.setColor(new Color(0, 87, 255));
        graphics2D.fillRect(0, (int) rightTop.y, (int) rightBottom.x+(int) (scaler.getScreenSize().x - rightTop.x), (int) rightBottom.y);

        graphics2D.setColor(new Color(255, 0, 203));
        graphics2D.fillRect(0, 0, (int) leftTop.x, (int) leftTop.y);
        graphics2D.setColor(new Color(137, 255, 244));
        graphics2D.fillRect((int) rightTop.x, 0, (int) (scaler.getScreenSize().x - rightTop.x), (int) leftTop.y);*/
    }

    public Graphics getPureGraphics() {
        return graphics2D;
    }
}
