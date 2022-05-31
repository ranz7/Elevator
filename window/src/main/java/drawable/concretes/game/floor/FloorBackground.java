package drawable.concretes.game.floor;

import settings.RoomRemoteSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

public class FloorBackground extends DrawableCreature {
    public FloorBackground(Vector2D floorSize, LocalDrawSetting settings) {
        super(new Vector2D(0, 0), floorSize,
                new Rectangle(settings.floorWallColor()),
                settings);
    }

    @Override
    public int getDrawPrioritet() {
        return 0;
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }
}
