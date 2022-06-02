package drawable.drawTool.figuresComponent;

import drawable.drawTool.DrawTool;
import tools.Vector2D;
import model.planes.graphics.Painter;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// in case of multiple figures
public class FiguresComponent extends DrawTool {
    List<Figure> figures = new LinkedList<>();

    public FiguresComponent(Figure... figures) {
        Arrays.stream(figures).forEach(figure -> this.figures.add(figure));
    }

    @Override
    public void draw(Vector2D position, Vector2D size, Painter drawer) {
        figures.forEach(figure -> figure.draw(position, size, drawer));
    }

    @Override
    public void setColor(Color color) {
        // wtf
    }
}
