package drawable.concretes.building.floor.elevatorSpace;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.withShape.creatures.DrawableLocalCreature;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

/*
 * Spaces behind elevators
 */
public class ElevatorBlackSpace extends DrawableLocalCreature {
    public ElevatorBlackSpace(Vector2D position, Vector2D size, CombienedDrawDataBase settings) {
        super(position, size,settings);
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 2;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.draw(this,settings.backGroundColor());
    }
}


