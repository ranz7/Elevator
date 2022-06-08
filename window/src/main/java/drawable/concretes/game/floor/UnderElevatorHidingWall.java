package drawable.concretes.game.floor;

import settings.RoomRemoteSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

public class UnderElevatorHidingWall extends DrawableCreature {
    public UnderElevatorHidingWall(Vector2D elevatorSize,Vector2D floorSize,LocalDrawSetting settings) {
        super(new Vector2D(0, elevatorSize.y),
                new Vector2D(floorSize.x, (floorSize.y - elevatorSize.y)),
                new Rectangle(settings.floorWallColor()),
                settings);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int getDrawPriority() {
        return 8;
    }
}



