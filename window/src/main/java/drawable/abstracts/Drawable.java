package drawable.abstracts;

import architecture.tickable.Tickable;
import configs.tools.CombienedDrawDataBase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tools.Pair;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public abstract class Drawable implements Tickable {
    final protected CombienedDrawDataBase settings;


    List<Drawable> subDrawables = new LinkedList<>();

    @Getter
    Vector2D realDrawPosition;

    public final List<Pair<Vector2D, Drawable>> getDrawables(Vector2D parentPosition) {
        var drawables = new LinkedList<Pair<Vector2D, Drawable>>();
        drawables.add(new Pair<>(parentPosition, this));
        var doubleParentPosition = getPosition().getAdded(parentPosition);
        for (var subDrawable : subDrawables) {
            drawables.add(new Pair<>(doubleParentPosition, subDrawable));
        }
        return drawables;
    }

    protected void addSubDrawable(Drawable drawable) {
        subDrawables.add(drawable);
    }

    public abstract void draw(GameDrawer drawer, GameDrawer gameDrawer);

    public abstract Integer GetDrawPrioritet();

    public abstract Vector2D getPosition();

    public abstract Vector2D getSize();

    public abstract boolean getIsVisible();

    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomCenter;
    }


}
