package drawable.drawTool.text;

import drawable.drawTool.DrawTool;
import drawable.drawTool.figuresComponent.Figure;
import lombok.Getter;
import lombok.Setter;
import tools.Vector2D;
import model.planes.graphics.Painter;

import java.awt.*;

public class Text extends Figure {
    @Setter
    private String text;

    public Text(String text, Color color) {
        super(color, new Vector2D(0.5, 0.5), new Vector2D(1, 1));
        this.text = text;
    }

    @Override
    public boolean isIntersect(Vector2D objectPosition, Vector2D objectSize, Vector2D gamePosition) {
        var tmp = afterProportionApply(objectPosition, objectSize);
        return gamePosition.isInside(tmp.getFirst(), tmp.getSecond());
    }

    @Override
    public void draw(Vector2D position, Vector2D size, Painter drawer) {
        var tmp = afterProportionApply(position, size);
        drawer.drawText(text, tmp.getFirst(), tmp.getSecond(), getMainColor());

        // TODO tutaj jak x jest 50 to text jest skrocony o 2 razy
    }

}
