package drawable.drawableObjectsConcrete.building.floor.decorations;

import drawable.drawableAbstract.drawableWithTexture.DrawCenter;
import drawable.drawableAbstract.drawableWithTexture.DrawableCreatureWithTexture;
import tools.Vector2D;

public class FloorPainting extends DrawableCreatureWithTexture {
    public FloorPainting(Vector2D position) {
        super(position, "/images/paintings/");
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 2;
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.CENTER_BY_XY;
    }
}