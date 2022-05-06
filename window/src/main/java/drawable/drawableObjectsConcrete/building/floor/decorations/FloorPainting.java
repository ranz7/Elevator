package drawable.drawableObjectsConcrete.building.floor.decorations;

import drawable.drawableBase.drawableWithTexture.DrawCenter;
import drawable.drawableBase.drawableWithTexture.DrawableWithTexture;
import tools.Vector2D;

public class FloorPainting extends DrawableWithTexture {
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