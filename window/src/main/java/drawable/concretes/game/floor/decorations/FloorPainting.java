package drawable.concretes.game.floor.decorations;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableRemoteCreature;
import drawable.drawTool.texture.Texture;
import settings.localDraw.LocalDrawSetting;
import java.util.Random;

public class FloorPainting extends DrawableRemoteCreature {
    public FloorPainting( LocalDrawSetting settings, Random random) {
        super( new Texture( "/images/paintings/", random), settings);
    }

    @Override
    public int getDrawPriority() {
        return 9;
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.center;
    }
}