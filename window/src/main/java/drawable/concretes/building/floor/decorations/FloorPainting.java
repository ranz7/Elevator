package drawable.concretes.building.floor.decorations;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.Texture;
import model.objects.Creature;
import tools.Vector2D;

import java.util.Random;

public class FloorPainting extends DrawableCreature {
    public FloorPainting(Vector2D position, CombienedDrawDataBase settings, Random random) {
        super(new Creature(position, new Vector2D(100, 100)), new Texture( "/images/paintings/", random), settings);
    }

    @Override
    public int GetDrawPrioritet() {
        return 2;
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.center;
    }
}