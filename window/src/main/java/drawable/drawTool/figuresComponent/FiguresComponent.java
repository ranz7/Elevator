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
    public Color getMainColor(){
        return null;
    }
    @Override
    public void setColor(Color color) {
        figures.forEach(figure->figure.setColor(color));
    }

    @Override
    public void setAdditionalLightColor(Color additionalLightColor) {
        figures.forEach(figure->figure.setAdditionalLightColor(additionalLightColor));
    }

    @Override
    public boolean isIntersect(Vector2D objectPosition, Vector2D objectSize, Vector2D gamePosition) {
        return figures.stream().anyMatch(figure -> figure.isIntersect(objectPosition, objectSize, gamePosition));
    }
}
