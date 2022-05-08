package drawable.drawableConcrete.building.floor;

import architecture.tickable.Tickable;
import configs.tools.CombienedDrawDataBase;
import drawable.drawableAbstract.DrawableLocalCreature;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

public class FloorWall extends DrawableLocalCreature implements Tickable {
    public FloorWall(Integer floorNumber, CombienedDrawDataBase settings) {
        super(new Vector2D(0, floorNumber * settings.floorHeight()),
                new Vector2D(settings.buildingSize().x, settings.floorHeight()), settings);
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 0;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.draw(this,settings.floorWallColor());
    }
}
