package drawable.drawTool.figuresComponent;

import drawable.drawTool.DrawTool;
import lombok.AllArgsConstructor;
import tools.Vector2D;

import java.awt.*;

@AllArgsConstructor
public abstract class Figure extends DrawTool {
    @Override
    public void setColor(Color color) {
        mainColor = color;
    }

    Color mainColor;
    Vector2D position; // FROM 0 to 100 - to jest proporcja
    Vector2D size; // FROM 0 to 100  - to jest proporcja
}
