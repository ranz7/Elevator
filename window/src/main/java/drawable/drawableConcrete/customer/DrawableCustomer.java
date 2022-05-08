package drawable.drawableConcrete.customer;

import configs.tools.CombienedDrawDataBase;
import drawable.drawableAbstract.DrawableRemoteCreature;
import lombok.Getter;
import lombok.Setter;
import model.objects.Creature;
import view.drawTools.drawer.GameDrawer;

import java.awt.*;

public class DrawableCustomer extends DrawableRemoteCreature {
    private final Color COLOR_OF_CUSTOMER;

    @Getter
    @Setter
    private boolean behindElevator = true;

    public DrawableCustomer(Creature creature, CombienedDrawDataBase settings) {
        super(creature, settings);
        var colors = settings.customerSkins();
        this.COLOR_OF_CUSTOMER = colors[(int) (getId() % colors.length)];
    }

    @Override
    public Integer GetDrawPrioritet() {
        if (behindElevator) {
            return 12;
        }
        return 5;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (!getIsVisible()) {
            behindElevator = false;
            return;
        }
        gameDrawer.draw(this, COLOR_OF_CUSTOMER, Color.DARK_GRAY, 2);
    }

    public boolean isNotBehindElevator() {
        return !behindElevator;
    }

    public void changeBehindElevator() {
        behindElevator = !behindElevator;
    }
}
