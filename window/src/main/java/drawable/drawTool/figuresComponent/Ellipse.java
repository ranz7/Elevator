package drawable.drawTool.figuresComponent;

import model.planes.graphics.Painter;
import tools.Vector2D;

import java.awt.*;

public class Ellipse extends Figure {
    public Ellipse(Color mainColor) {
        super(mainColor, new Vector2D(0.5, 0.5), new Vector2D(1, 1));
    }

    @Override
    public void draw(Vector2D position, Vector2D size, Painter drawer) {
        var tmp = afterProportionApply(position,size);
        drawer.drawFilledEllipse(tmp.getFirst(), tmp.getSecond(), mainColor, mainColor, 0);
    }
}
