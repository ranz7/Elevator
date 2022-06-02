package drawable.drawTool.figuresComponent;

import tools.Vector2D;
import model.planes.graphics.Painter;

import java.awt.*;

public class RectangleWithBorder extends Figure {
    private final Double borderThickness;
    private final Color secondColor;

    public RectangleWithBorder(Color color, Color secondColor, double borderThickness) {
        super(color, new Vector2D(0.5, 0.5), new Vector2D(1, 1));
        this.borderThickness = borderThickness;
        this.secondColor = secondColor;
    }

    public RectangleWithBorder(Color secondColor, double borderThickness) {
        super(secondColor, new Vector2D(50, 50), new Vector2D(100, 100));
        this.borderThickness = borderThickness;
        this.secondColor = null;
    }

    @Override
    public void draw(Vector2D position, Vector2D size, Painter drawer) {
        var tmp = afterProportionApply(position,size);
        if (secondColor == null) { //without fill
            drawer.drawOnlyBorderRect(tmp.getFirst(),tmp.getSecond(), mainColor, borderThickness);
        } else {
            drawer.drawFilledRect(tmp.getFirst(),tmp.getSecond(), mainColor, secondColor, borderThickness);
        }
    }


}


