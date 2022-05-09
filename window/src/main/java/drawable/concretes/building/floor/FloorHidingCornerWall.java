package drawable.concretes.building.floor;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.withShape.creatures.DrawableLocalCreature;
import drawable.abstracts.DrawCenter;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

public class FloorHidingCornerWall extends DrawableLocalCreature {
    public FloorHidingCornerWall(Vector2D position, Vector2D size, CombienedDrawDataBase settings) {
        super(position, size, settings);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.draw(this, settings.backGroundColor());
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 14;
    }
}
