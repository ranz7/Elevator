package drawable.concretes.game.floor.decorations;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.texture.Texture;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

import java.util.Random;

public class MenuPainting extends DrawableCreature {
    public MenuPainting(Vector2D position, LocalDrawSetting settings, Random random) {
        super(position, new Vector2D(100, 100),
                new Texture("/images/paintings/", random),
                settings);
    }

    @Override
    public int getDrawPriority() {
        return -5;
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }
}
