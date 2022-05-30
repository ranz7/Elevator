package drawable.concretes.game.floor.decorations;

import settings.CombienedDrawSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.texture.Texture;
import model.objects.Creature;
import tools.Vector2D;

import java.util.Random;

public class FloorPainting extends DrawableCreature {
    public FloorPainting(Vector2D position, CombienedDrawSettings settings, Random random) {
        super( position, new Vector2D(100, 100), new Texture( "/images/paintings/", random), settings);
    }

    @Override
    public int GetDrawPrioritet() {
        return 9;
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.center;
    }
}