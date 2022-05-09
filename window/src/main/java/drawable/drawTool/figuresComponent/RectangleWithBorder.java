package drawable.drawTool.figuresComponent;

import tools.Vector2D;

import java.awt.*;

public class RectangleWithBorder extends Figure {
    private  final  Integer borderThickness;
    private final Color borderColor;

    public RectangleWithBorder(Color color, Vector2D position, Vector2D size, Color borderColor, Integer borderThickness) {
        super(color, position, size);
        this.borderThickness = borderThickness;
        this.borderColor = borderColor;
    }

    @Override
    protected void draw() {

    }
}
