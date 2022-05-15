package drawable.abstracts;

import architecture.tickable.Tickable;
import databases.CombienedDrawDataBase;
import drawable.drawTool.DrawTool;
import lombok.Getter;
import model.objects.Creature;
import tools.Pair;
import tools.Vector2D;
import view.graphics.GameGraphics;

import java.util.LinkedList;
import java.util.List;

public abstract class DrawableCreature implements Tickable {
    final protected Creature creature;
    @Getter
    final private DrawTool tool;
    final protected CombienedDrawDataBase dataBase;

    @Getter
    Vector2D realDrawPosition;

    // Drawable collection
    List<DrawableCreature> subDrawableCreatures = new LinkedList<>();

    public final List<Pair<Vector2D, DrawableCreature>> getDrawablesAndAbsoluteDrawPositions(Vector2D parentPosition) {
        var drawables = new LinkedList<Pair<Vector2D, DrawableCreature>>();
        drawables.add(new Pair<>(parentPosition, this));
        var doubleParentPosition = getPosition().getAdded(parentPosition);
        for (var subDrawable : subDrawableCreatures) {
            drawables.add(new Pair<>(doubleParentPosition, subDrawable));
        }
        return drawables;
    }

    public final void addSubDrawable(DrawableCreature drawableCreature) {
        subDrawableCreatures.add(drawableCreature);
    }

    public DrawableCreature(Creature creature, DrawTool tool, CombienedDrawDataBase settings) {
        this.tool = tool;
        this.dataBase = settings;
        this.creature = creature;
    }

    public final void draw(Vector2D realDrawPosition, GameGraphics gameDrawer) {
        this.realDrawPosition = realDrawPosition; // TODO check if works
        if (!getIsVisible()) {
            return;
        }

        Vector2D positionOfTheCreature = getDrawCenter().getShiftDrawPosition(getPosition(), getSize());
        getTool().draw(positionOfTheCreature.getAdded(realDrawPosition), getSize(), gameDrawer);
    }

    public final Vector2D getPosition() {
        return creature.getPosition();
    }

    public final Vector2D getSize() {
        return creature.getSize();
    }

    public final boolean getIsVisible() {
        return creature.isVisible();
    }


    protected void setSize(Vector2D size) {
        creature.setSize(size);
    }

    protected long getId() {
        return creature.getId();
    }

    public abstract DrawCenter getDrawCenter();

    public abstract int GetDrawPrioritet();
}
