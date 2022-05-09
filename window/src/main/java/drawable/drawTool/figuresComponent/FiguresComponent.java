package drawable.drawTool.figuresComponent;

import drawable.drawTool.DrawTool;
import model.objects.Creature;
import view.drawTools.drawer.GameDrawer;

import java.util.Arrays;
import java.util.LinkedList;

public class FiguresComponent extends DrawTool {
    LinkedList<Figure> figures;

    public FiguresComponent(Figure... figures) {
        Arrays.stream(figures).forEach(figure -> this.figures.add(figure));
    }

    @Override
    public void draw(GameDrawer drawer, Creature creature) {
        // CO DALEJ
    }
}
