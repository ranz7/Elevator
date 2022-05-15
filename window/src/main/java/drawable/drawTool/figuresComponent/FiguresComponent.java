package drawable.drawTool.figuresComponent;

import drawable.drawTool.DrawTool;
import tools.Vector2D;
import view.graphics.GameGraphics;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;

// in case of multiple figures
public class FiguresComponent extends DrawTool {
    LinkedList<Figure> figures;

    public FiguresComponent(Figure... figures) {
        Arrays.stream(figures).forEach(figure -> this.figures.add(figure));
    }

    @Override
    public void draw(Vector2D position, Vector2D size, GameGraphics drawer) {

    }

    @Override
    public void setColor(Color color) {
        // wtf
    }
}
