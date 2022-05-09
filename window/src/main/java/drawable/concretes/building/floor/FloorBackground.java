package drawable.concretes.building.floor;

import architecture.tickable.Tickable;
import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.withShape.creatures.DrawableLocalCreature;
import drawable.abstracts.DrawCenter;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

public class FloorBackground extends DrawableLocalCreature implements Tickable {
    public FloorBackground(Integer floorNumber, CombienedDrawDataBase settings) {
        super(new Vector2D(0, floorNumber * settings.floorHeight()),
                new Vector2D(settings.buildingSize().x, settings.floorHeight()), settings);
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 0;
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.draw(this,settings.floorWallColor());
    }
}
