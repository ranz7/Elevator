package drawable.drawableObjects.customer;

import drawable.Drawable;

import model.objects.movingObject.Creature;
import view.drawTools.GameDrawer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class DrawableCustomer extends Creature implements Drawable {
    private final Color COLOR_OF_CUSTOMER;

    public DrawableCustomer(Creature creature, Color[] color) {
        super(creature);
        this.COLOR_OF_CUSTOMER = color[(int) (getId() % color.length)];
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 12;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (!isVisible()) {
            return;
        }
        gameDrawer.setColor(COLOR_OF_CUSTOMER);
        gameDrawer.fillRect(this.position, this.size, Color.DARK_GRAY, 2);
    }

    @Override
    public List<Drawable> getDrawables() {
        var drawables = new LinkedList<Drawable>();
        drawables.add(this);
        return drawables;

    }

    @Override
    public void tick(long deltaTime) {
    }
}
