package drawable.concretes.building.floor.elevatorSpace;

import databases.CombienedDrawDataBase;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import model.objects.Creature;
import tools.Vector2D;

/*
 * Spaces behind elevators
 */
public class ElevatorBlackSpace extends DrawableCreature {
    public ElevatorBlackSpace(Vector2D size, CombienedDrawDataBase settings) {
        super(new Creature(new Vector2D(0, 0), size), new Rectangle(settings.backGroundColor()), settings);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int GetDrawPrioritet() {
        return 2;
    }


}


