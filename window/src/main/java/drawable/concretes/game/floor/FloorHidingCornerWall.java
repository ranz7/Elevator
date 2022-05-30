package drawable.concretes.game.floor;

import settings.CombienedDrawSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import model.objects.Creature;
import tools.Vector2D;

public class FloorHidingCornerWall extends DrawableCreature {
    public FloorHidingCornerWall(Vector2D position, Vector2D size, CombienedDrawSettings settings) {
        super(position, size, new Rectangle(settings.backGroundColor()), settings);
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