package drawable.drawableObjects.building;

import drawable.Drawable;
import model.objects.movingObject.Creature;
import tools.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class HidingWall extends Creature implements Drawable {
    private final Color BACK_GROUND_COLOR;

    public HidingWall(Vector2D position, Point size, Color backGroundColor) {
        super(position, size);
        this.BACK_GROUND_COLOR = backGroundColor;
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 8;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(BACK_GROUND_COLOR);
        gameDrawer.fillRect(this);
    }

    public void tick(long delta_time){}

    @Override
    public List<Drawable> getDrawables() {
        var drawables = new LinkedList<Drawable>();
        drawables.add(this);
        return drawables;
    }
}



