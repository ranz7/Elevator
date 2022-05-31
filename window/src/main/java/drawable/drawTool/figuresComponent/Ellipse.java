package drawable.drawTool.figuresComponent;

import model.planes.graphics.Painter;
import tools.Vector2D;

import java.awt.*;

public class Ellipse extends Figure {
    public Ellipse(Color mainColor) {
        super(mainColor, new Vector2D(0, 0), new Vector2D(0, 0));
    }

    @Override
    public void draw(Vector2D position, Vector2D size, Painter drawer) {
        drawer.drawFilledEllipse(position, size, mainColor, mainColor, 0);
    }
}
