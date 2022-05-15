package drawable.drawTool.figuresComponent;

import drawable.abstracts.Drawable;
import tools.Vector2D;
import view.graphics.GameGraphics;

import java.awt.*;

public class Rectangle extends Figure {
    public Rectangle(Color color) {
        super(color, new Vector2D(0, 0), new Vector2D(0, 0));
    }

    public Rectangle(Color color, Vector2D position, Vector2D size) {
        super(color, position, size);
    }

    @Override
    public void draw(Vector2D position, Vector2D size, GameGraphics drawer) {
        drawer.drawFilledRect(position, size, mainColor, mainColor, 0);
    }
}
