package drawable.concretes.game.customer;

import settings.CombienedDrawSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableRemoteCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import lombok.Getter;
import lombok.Setter;
import model.objects.Creature;
import tools.Vector2D;

public class DrawableCustomer extends DrawableRemoteCreature {
    @Getter
    @Setter
    private boolean behindElevator = true;

    public DrawableCustomer(CombienedDrawSettings settings) {
        super(new Rectangle(settings.getRandomCustomerSkin()), settings);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomCenter;
    }

    @Override
    public int GetDrawPrioritet() {
        if (behindElevator) {
            return 12;
        }
        return 5;
    }

    public void changeBehindElevator() {
        behindElevator = !behindElevator;
    }
}
