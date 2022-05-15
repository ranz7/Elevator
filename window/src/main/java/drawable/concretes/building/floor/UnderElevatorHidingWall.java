package drawable.concretes.building.floor;

import settings.CombienedDrawSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import model.objects.Creature;
import tools.Vector2D;

public class UnderElevatorHidingWall extends DrawableCreature {
    public UnderElevatorHidingWall( CombienedDrawSettings settings) {
        super(new Creature( new Vector2D(0,settings.elevatorSize().y),
                new Vector2D(settings.buildingSize().x , (settings.floorHeight() - settings.elevatorSize().y))),
                new Rectangle(settings.floorWallColor()),
                settings);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int GetDrawPrioritet() {
        return 8;
    }
}



