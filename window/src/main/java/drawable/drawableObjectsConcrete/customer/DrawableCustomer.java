package drawable.drawableObjectsConcrete.customer;

import configs.CanvasSettings.MainSettings;
import drawable.drawableBase.Drawable;
import lombok.Getter;
import lombok.Setter;
import model.objects.Creature;
import view.drawTools.GameDrawer;

import java.awt.*;

public class DrawableCustomer extends Drawable {
    private final Color COLOR_OF_CUSTOMER;

    @Getter
    @Setter
    private boolean behindElevator = true;

    public DrawableCustomer(Creature creature, MainSettings settings) {
        super(creature);
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
