package drawable.abstracts;

import architecture.tickable.Tickable;
import configs.tools.CombienedDrawDataBase;
import drawable.drawTool.DrawTool;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tools.Pair;
import tools.Vector2D;
import view.graphics.GameGraphics;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public abstract class Drawable implements Tickable {
    @Getter
    final private DrawTool tool;
    final protected CombienedDrawDataBase dataBase;

    List<Drawable> subDrawableCreatures = new LinkedList<>();

    @Getter
    Vector2D realDrawPosition;

    public abstract Vector2D getPosition();

    public abstract Vector2D getSize();

    public abstract boolean getIsVisible();

    public abstract DrawCenter getDrawCenter();

    public abstract int GetDrawPrioritet();

    public abstract void draw(Vector2D realDrawPosition, GameGraphics gameDrawer);

    public Vector2D getShiftDrawPosition(Vector2D position, Vector2D size, DrawCenter drawCenter) {
        return switch (drawCenter) {
            case bottomCenter -> position.getSubbed(new Vector2D(size.x / 2., 0));
            case leftCenter -> position.getSubbed(new Vector2D(0, size.y / 2.));
            case center -> position.getSubbed(new Vector2D(size.x / 2., size.y / 2.));
            case bottomRight -> position.getSubbed(new Vector2D(size.x, 0));
            case bottomLeft -> position.getSubbed(new Vector2D(0, 0));
        };
    }

    public final List<Pair<Vector2D, Drawable>> getDrawables(Vector2D parentPosition) {
        var drawables = new LinkedList<Pair<Vector2D, Drawable>>();
        drawables.add(new Pair<>(parentPosition, this));
        var doubleParentPosition = getPosition().getAdded(parentPosition);
        for (var subDrawable : subDrawableCreatures) {
            drawables.add(new Pair<>(doubleParentPosition, subDrawable));
        }
        return drawables;
    }

    protected final void addSubDrawable(Drawable drawableCreature) {
        subDrawableCreatures.add(drawableCreature);
    }
}
