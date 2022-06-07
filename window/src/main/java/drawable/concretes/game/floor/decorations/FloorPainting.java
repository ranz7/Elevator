package drawable.concretes.game.floor.decorations;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableRemoteCreature;
import drawable.drawTool.texture.Texture;
import model.packageLoader.DrawableCreatureData;
import settings.localDraw.LocalDrawSetting;
import java.util.Random;

public class FloorPainting extends DrawableRemoteCreature {
    public FloorPainting(DrawableCreatureData creatureData, LocalDrawSetting settings, Integer numberOfPainting) {
        super(creatureData, new Texture( "/images/paintings/", new Random(numberOfPainting)), settings);
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