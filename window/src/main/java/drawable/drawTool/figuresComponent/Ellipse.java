package drawable.drawTool.figuresComponent;

import model.planes.graphics.Painter;
import tools.Vector2D;

import java.awt.*;

public class Ellipse extends Figure {
    public Ellipse(Color mainColor) {
        super(mainColor, new Vector2D(0.5, 0.5), new Vector2D(1, 1));
    }


    @Override
    public boolean isIntersect(Vector2D objectPosition, Vector2D objectSize, Vector2D gamePosition) {
        var tmp = afterProportionApply(objectPosition, objectSize);

        return gamePosition.isInsideEllipse(tmp.getFirst(),tmp.getSecond());
    }
    @Override
    public void draw(Vector2D position, Vector2D size, Painter drawer) {
        var tmp = afterProportionApply(position,size);
        drawer.drawFilledEllipse(tmp.getFirst(), tmp.getSecond(), getMainColor(), new Color(0,0,0), 1);
    }
}
