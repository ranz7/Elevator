package drawable.concretes.building.floor;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import model.objects.Creature;
import tools.Vector2D;

public class FloorHidingCornerWall extends DrawableCreature {
    public FloorHidingCornerWall(Vector2D position, Vector2D size, CombienedDrawDataBase settings) {
        super(new Creature(position, size), new Rectangle(settings.backGroundColor()), settings);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int GetDrawPrioritet() {
        return 14;
    }
}
