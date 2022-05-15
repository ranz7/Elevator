package drawable.concretes.customer;

import databases.CombienedDrawDataBase;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableRemoteCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import lombok.Getter;
import lombok.Setter;
import model.objects.Creature;

public class DrawableCustomerCreature extends DrawableRemoteCreature {
    @Getter
    @Setter
    private boolean behindElevator = true;

    public DrawableCustomerCreature(Creature creature, CombienedDrawDataBase settings) {
        super(creature,new Rectangle(settings.getRandomCustomerSkin()), settings);
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
