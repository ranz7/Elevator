package drawable.drawableObjects;

import drawable.Drawable;

import model.objects.movingObject.Creature;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class DrawableCustomer extends Creature implements Drawable {

    @Getter
    @Setter
    private boolean behindElevator = true;
    @Getter
    @Setter
    private double serverRespondTime;

    public DrawableCustomer(Creature creature, Color[] color) {
        super(creature);
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (!isVisible()) {
            behindElevator = false;
            return;
        }
        g2d.setColor(new Color(124,123,12));
        g2d.fillRect((int)this.position.x,(int)this.position.y, this.size.x, this.size.y);
    }

    @Override
    public void tick(long deltaTime) {
        if (!isVisible()) {
            return;
        }
    }
}
