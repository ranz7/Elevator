package drawable.concretes.game.customer;

import drawable.abstracts.Drawable;
import model.Transport;
import model.Transportable;
import model.packageLoader.DrawableCreatureData;
import settings.RoomRemoteSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableRemoteCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import lombok.Getter;
import lombok.Setter;
import settings.localDraw.LocalDrawSetting;

public class DrawableCustomer extends DrawableRemoteCreature implements Transportable<Drawable> {
    @Getter
    @Setter
    private boolean behindElevator = true;
    @Setter
    @Getter
    Transport<Drawable> transport; // floor or elevator

    public DrawableCustomer(DrawableCreatureData creatureData, LocalDrawSetting settings) {
        super(creatureData, new Rectangle(settings.getRandomCustomerSkin()), settings);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomCenter;
    }

    @Override
    public int getDrawPriority() {
        if (behindElevator) {
            return 12;
        }
        return 5;
    }

    public void changeBehindElevator() {
        behindElevator = !behindElevator;
    }


}
