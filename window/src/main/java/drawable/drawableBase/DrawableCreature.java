package drawable.drawableBase;

import model.objects.movingObject.Creature;
import common.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class DrawableCreature extends Creature implements Drawable {
    public DrawableCreature(Vector2D position) {
        super(position);
    }

    public DrawableCreature(Vector2D position, Point size) {
        super(position, size);
    }

    public DrawableCreature(Creature creatureA) {
        super(creatureA);
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
    }

    @Override
    public void tick(long delta_time) {
    }

    @Override
    public List<Drawable> getDrawables() {
        var drawables = new LinkedList<Drawable>();
        drawables.add(this);
        return drawables;
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 10000;
    }

    public DrawCenter getDrawCenter(){
        return DrawCenter.CENTER_BY_X;
    }
}
