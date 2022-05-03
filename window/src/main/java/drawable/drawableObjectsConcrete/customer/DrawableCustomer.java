package drawable.drawableObjectsConcrete.customer;

import drawable.drawableBase.DrawableCreature;
import lombok.Getter;
import lombok.Setter;
import model.objects.movingObject.Creature;
import view.drawTools.GameDrawer;

import java.awt.*;

public class DrawableCustomer extends DrawableCreature {
    private final Color COLOR_OF_CUSTOMER;

    @Getter
    @Setter
    private boolean behindElevator = true;

    public DrawableCustomer(Creature creature, Color[] color) {
        super(creature);
        this.COLOR_OF_CUSTOMER = color[(int) (getId() % color.length)];
    }

    @Override
    public Integer GetDrawPrioritet() {
        if (behindElevator) {
            return 4;
        }
        return 12;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (!isVisible()) {
            behindElevator = false;
            return;
        }
        gameDrawer.setColor(COLOR_OF_CUSTOMER);
        gameDrawer.drawFilledRect(this.position, this.size, Color.DARK_GRAY, 2);
    }

    public boolean isNotBehindElevator() {
        return !behindElevator;
    }

    public void changeBehindElevator() {
        behindElevator = !behindElevator;
    }
}
