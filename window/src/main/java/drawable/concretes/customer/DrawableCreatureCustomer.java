package drawable.concretes.customer;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreatureRemote;
import drawable.drawTool.figuresComponent.Rectangle;
import lombok.Getter;
import lombok.Setter;
import model.objects.Creature;

public class DrawableCreatureCustomer extends DrawableCreatureRemote {
    @Getter
    @Setter
    private boolean behindElevator = true;

    public DrawableCreatureCustomer(Creature creature, CombienedDrawDataBase settings) {
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
