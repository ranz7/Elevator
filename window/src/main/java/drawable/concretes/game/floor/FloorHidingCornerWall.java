package drawable.concretes.game.floor;

import settings.RoomRemoteSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

public class FloorHidingCornerWall extends DrawableCreature {
    public FloorHidingCornerWall(Vector2D position, Vector2D size, LocalDrawSetting settings) {
        super(position, size, new Rectangle(settings.backGroundColor()), settings);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int getDrawPrioritet() {
        return 14;
    }
}
