package drawable.drawTool;

import tools.Vector2D;
import view.graphics.GameGraphics;

import java.awt.*;

public abstract class DrawTool {
    public abstract void draw(Vector2D position, Vector2D size, GameGraphics drawer);

    public abstract void setColor(Color color);
}
