package drawable.drawableObjects;

import drawable.Drawable;

import model.objects.movingObject.Creature;
import view.GameDrawer;

import java.awt.*;

public class DrawableCustomer extends Creature implements Drawable {
    private final Color COLOR_OF_CUSTOMER;

    public DrawableCustomer(Creature creature, Color[] color) {
        super(creature);
        this.COLOR_OF_CUSTOMER = color[(int) (getId() % color.length)];
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
    public void tick(long deltaTime) {
    }
}
