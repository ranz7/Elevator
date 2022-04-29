package drawable.drawableObjects;

import drawable.Drawable;

import model.objects.movingObject.Creature;
import view.GameDrawer;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class DrawableCustomer extends Creature implements Drawable {
    private final Color COLOR_OF_CUSTOMER;

    @Getter
    @Setter
    private boolean behindElevator = true;
    @Getter
    @Setter
    private double serverRespondTime;

    public DrawableCustomer(Creature creature) {
        super(creature);
        this.COLOR_OF_CUSTOMER = new Color(201, 143, 90);
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (!isVisible()) {
            behindElevator = false;
            return;
        }
        gameDrawer.setColor(COLOR_OF_CUSTOMER);
        gameDrawer.fillRect(this.position, this.size, Color.DARK_GRAY, 2);
    }

    @Override
    public void tick(long deltaTime) {
    }

}
