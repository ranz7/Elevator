package drawable.concretes.game.floor;

import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableRemoteCreature;
import lombok.Getter;
import lombok.Setter;
import model.DatabaseOf;
import model.Transport;
import model.Transportable;
import model.packageLoader.DrawableCreatureData;
import protocol.special.CreatureType;
import settings.RoomRemoteSettings;
import drawable.abstracts.DrawCenter;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

public class DrawableFloorStructure extends DrawableRemoteCreature implements Transport<Drawable>, Transportable<Drawable> {
    @Setter
    @Getter
    private Transport<Drawable> transport;
    @Getter
    private final DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this,
            FloorHidingCornerWall.class);

    public DrawableFloorStructure(DrawableCreatureData data, LocalDrawSetting settings) {
        super(data, new RectangleWithBorder(settings.florBetonColor(), 7), settings);


        add(new FloorHidingCornerWall(
                new Vector2D(0 - getSize().x * 4., -2),
                new Vector2D(getSize().x * 4, getSize().y + 2),
                settings
        ));
        add(new FloorHidingCornerWall(
                new Vector2D(getSize().x, -2),
                new Vector2D(settings.customerWidth() * 4, getSize().y + 2),
                settings
        ));
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int getDrawPriority() {
        return 14;
    }

    public boolean isBottomFloor() {
        return !(transport instanceof DrawableFloorStructure);
    }

    public int getCurrentFloorNum() {
        if (isBottomFloor()) {
            return 0;
        }
        return ((DrawableFloorStructure) transport).getCurrentFloorNum() + 1;
    }

    @Override
    public void add(Drawable drawable) {
        if (drawable instanceof DrawableCreatureData) {
            if (((DrawableCreatureData) drawable).getCreatureType() == CreatureType.FLOOR) {
                drawable = new DrawableFloorStructure((DrawableCreatureData) drawable, getSettings());
            }
        }
        localDataBase.addCreature(drawable);
    }
}
