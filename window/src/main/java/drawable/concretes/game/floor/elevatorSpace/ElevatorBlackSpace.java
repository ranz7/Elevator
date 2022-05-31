package drawable.concretes.game.floor.elevatorSpace;

import settings.RoomRemoteSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

/*
 * Spaces behind elevators
 */
public class ElevatorBlackSpace extends DrawableCreature {
    public ElevatorBlackSpace(Vector2D size, LocalDrawSetting settings) {
        super(new Vector2D(0, 0), size, new Rectangle(settings.backGroundColor()), settings);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int getDrawPrioritet() {
        return 2;
    }


}


