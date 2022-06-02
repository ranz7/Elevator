package drawable.drawTool;

import tools.Vector2D;
import model.planes.graphics.Painter;

import java.awt.*;

public abstract class DrawTool {
    public abstract void draw(Vector2D position, Vector2D size, Painter drawer);
    public abstract void setColor(Color color);
}
