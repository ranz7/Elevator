package drawable.concretes.game.floor;

import settings.RoomRemoteSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

public class UnderElevatorHidingWall extends DrawableCreature {
    public UnderElevatorHidingWall( LocalDrawSetting settings) {
//        super(  new Vector2D(0,settings.elevatorSize().y),
//                new Vector2D(settings.buildingSize().x , (settings.floorHeight() - settings.elevatorSize().y)),
                super(new Vector2D(0,0),new Vector2D(200,10),new Rectangle(settings.floorWallColor()),
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



