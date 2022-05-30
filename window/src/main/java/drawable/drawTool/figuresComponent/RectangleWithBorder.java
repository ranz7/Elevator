package drawable.drawTool.figuresComponent;

import tools.Vector2D;
import model.planes.graphics.Painter;

import java.awt.*;

public class RectangleWithBorder extends Figure {
    private final Double borderThickness;
    private final Color secondColor;

    public RectangleWithBorder(Color color, Color secondColor, double borderThickness) {
        super(color, new Vector2D(100, 100), new Vector2D(100, 100));
        this.borderThickness = borderThickness;
        this.secondColor = secondColor;
    }

    public RectangleWithBorder(Color secondColor, double borderThickness) {
        super(secondColor, new Vector2D(100, 100), new Vector2D(100, 100));
        this.borderThickness = borderThickness;
        this.secondColor = null;
    }

    @Override
    public void draw(Vector2D position, Vector2D size, Painter drawer) {
        if(secondColor==null){ //without fill
            drawer.drawOnlyBorderRect(position, size, mainColor, borderThickness);
        }else{
            drawer.drawFilledRect(position, size, mainColor, secondColor, borderThickness);
        }
    }



}


