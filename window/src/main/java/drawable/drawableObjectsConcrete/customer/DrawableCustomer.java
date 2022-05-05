package drawable.drawableObjectsConcrete.customer;

import drawable.drawableBase.creatureWithTexture.DrawableCreature;
import model.objects.movingObject.Creature;
import view.drawTools.GameDrawer;

import java.awt.*;

public class DrawableCustomer extends DrawableCreature {
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
        gameDrawer.drawFilledRect(this.position, this.size, Color.DARK_GRAY, 2);
    }
}
