package drawable.drawTool.figuresComponent;

import tools.Vector2D;
import model.planes.graphics.Painter;
import view.buttons.MutableColor;

import java.awt.*;

public class RectangleWithBorder extends Figure {
    private final Double borderThickness;
    private final MutableColor secondColor;

    public RectangleWithBorder(MutableColor color, MutableColor secondColor, double borderThickness) {
        super(color, new Vector2D(0.5, 0.5), new Vector2D(1, 1));
        this.borderThickness = borderThickness;
        this.secondColor = secondColor;
    }

    @Override
    public boolean isIntersect(Vector2D objectPosition, Vector2D objectSize, Vector2D gamePosition) {
        var tmp = afterProportionApply(objectPosition, objectSize);
        if (secondColor == null) {
            return gamePosition.isInside(tmp.getFirst(), tmp.getSecond()) &&
                    !gamePosition.isInside(tmp.getFirst().add(borderThickness), tmp.getSecond().sub(borderThickness * 2));
        }
        return gamePosition.isInside(tmp.getFirst(), tmp.getSecond());
    }

    public RectangleWithBorder(MutableColor secondColor, double borderThickness) {
        super(secondColor, new Vector2D(0.5, 0.5), new Vector2D(1, 1));
        this.borderThickness = borderThickness;
        this.secondColor = null;
    }

    @Override
    public void draw(Vector2D position, Vector2D size, Painter drawer) {
        var tmp = afterProportionApply(position, size);
        if (secondColor == null) { //without fill
            drawer.drawOnlyBorderRect(tmp.getFirst(), tmp.getSecond(), getMainColor().getColor(), borderThickness);
        } else {
            drawer.drawFilledRect(tmp.getFirst(), tmp.getSecond(), getMainColor().getColor(), secondColor.getColor(), borderThickness);
        }
    }


}


