package drawable.drawableObjectsConcrete.building.floor.decorations;

import drawable.drawableBase.creatureWithTexture.DrawableCreatureWithTexture;
import tools.Vector2D;

public class FloorPainting extends DrawableCreatureWithTexture {
    public FloorPainting(Vector2D position) {
        super(position, "/images/paintings/");
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 12;
    }
}
