package drawable.concretes.building.floor;

import settings.CombienedDrawSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import model.objects.Creature;
import tools.Vector2D;

public class FloorBackground extends DrawableCreature {
    public FloorBackground(CombienedDrawSettings settings) {
        super(new Creature(new Vector2D(0, 0), new Vector2D(settings.buildingSize().x, settings.floorHeight())),
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
