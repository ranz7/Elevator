package drawable.drawTool.figuresComponent;

import tools.Vector2D;
import model.planes.graphics.Painter;

import java.awt.*;

public class Rectangle extends Figure {
    public Rectangle(Color color) {
        super(color,  new Vector2D(0.5, 0.5), new Vector2D(1, 1));
    }

    public Rectangle(Color color, Vector2D position, Vector2D size) {
        super(color, position, size);
    }

    @Override
    public void draw(Vector2D position, Vector2D size, Painter drawer) {
        var tmp = afterProportionApply(position,size);
        drawer.drawFilledRect(tmp.getFirst() ,tmp.getSecond(), mainColor, mainColor, 0);
    }
}
