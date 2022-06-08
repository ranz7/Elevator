package drawable.drawTool;

import lombok.Getter;
import lombok.Setter;
import tools.Vector2D;
import model.planes.graphics.Painter;

import java.awt.*;

public abstract class DrawTool {
    @Getter
    @Setter
    Color additionalLightColor = new Color(0, 0, 0);

    public abstract Color getMainColor();
    public abstract void draw(Vector2D position, Vector2D size, Painter drawer);
    public abstract void setColor(Color color);
    public abstract boolean isIntersect(Vector2D objectPosition, Vector2D objectSize, Vector2D gamePosition);
}
