package drawable.concretes.game.floor;

import settings.CombienedDrawSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import model.objects.Creature;
import tools.Vector2D;

public class FloorBackground extends DrawableCreature {
    public FloorBackground(Vector2D floorSize, CombienedDrawSettings settings) {
        super(new Vector2D(0, 0), floorSize,
                new Rectangle(settings.floorWallColor()),
                settings);
    }

    @Override
    public int GetDrawPrioritet() {
        return 0;
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }
}
