package drawable.drawTool.figuresComponent;

import tools.Vector2D;
import model.planes.graphics.Painter;
import view.buttons.MutableColor;

import java.awt.*;

public class Rectangle extends Figure {
    public Rectangle(MutableColor color) {
        super(color, new Vector2D(0.5, 0.5), new Vector2D(1, 1));
    }

    public Rectangle(Vector2D position, Vector2D size, MutableColor color) {
        super(color, position, size);
    }

    @Override
    public boolean isIntersect(Vector2D objectPosition, Vector2D objectSize, Vector2D gamePosition) {
        var tmp = afterProportionApply(objectPosition, objectSize);
        return gamePosition.isInside(tmp.getFirst(), tmp.getSecond());
    }

    @Override
    public void draw(Vector2D position, Vector2D size, Painter drawer) {
        var tmp = afterProportionApply(position, size);
        drawer.drawFilledRect(tmp.getFirst(),
                tmp.getSecond(),
                getMainColor().getColor(),
                getMainColor().getColor(),
                0
        );
    }
}
