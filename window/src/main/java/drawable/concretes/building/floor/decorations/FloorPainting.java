package drawable.concretes.building.floor.decorations;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.withTexture.DrawableCreatureWithTexture;
import tools.Vector2D;

import java.util.Random;

public class FloorPainting extends DrawableCreatureWithTexture {
    public FloorPainting(Vector2D position, CombienedDrawDataBase settings, Random random) {
        super(position, "/images/paintings/", settings, random);
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 2;
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.center;
    }
}