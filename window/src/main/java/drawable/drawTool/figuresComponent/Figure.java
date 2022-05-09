package drawable.drawTool.figuresComponent;

import lombok.AllArgsConstructor;
import tools.Vector2D;

import java.awt.*;

@AllArgsConstructor
public abstract class Figure {
    protected abstract void draw(); // JAKIE PARAMETRY ?

    Color color;
    Vector2D position; // FROM 0 to 100 - to jest proporcja
    Vector2D size; // FROM 0 to 100  - to jest proporcja
}
