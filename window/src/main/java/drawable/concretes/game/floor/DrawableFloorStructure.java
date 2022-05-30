package drawable.concretes.game.floor;

import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableRemoteCreature;
import lombok.Getter;
import lombok.Setter;
import model.DatabaseOf;
import model.Transport;
import model.Transportable;
import settings.CombienedDrawSettings;
import drawable.abstracts.DrawCenter;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import tools.Vector2D;

public class DrawableFloorStructure extends DrawableRemoteCreature implements Transport, Transportable {
    @Setter
    @Getter
    private Transport transport;
    @Getter
    private final DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this);

    public DrawableFloorStructure(CombienedDrawSettings settings) {
        super(new RectangleWithBorder(settings.florBetonColor(), 7), settings);

        var customerSize = settings.customerSize();

        localDataBase.add(new FloorHidingCornerWall(
                new Vector2D(0 - getSize().x * 4., -2),
                new Vector2D(getSize().x * 4, getSize().y + 2),
                settings
        ));
        localDataBase.add(new FloorHidingCornerWall(
                new Vector2D(getSize().x, -2),
                new Vector2D(customerSize.x * 4, getSize().y + 2),
                settings
        ));
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int GetDrawPrioritet() {
        return 14;
    }

    public boolean isBottomFloor() {
        return !(transport instanceof DrawableFloorStructure);
    }

    public int getCurrentFloorNum() {
        if(isBottomFloor()){
            return 0;
        }
        return ((DrawableFloorStructure) transport).getCurrentFloorNum() + 1;
    }
}
