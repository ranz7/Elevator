package drawable.abstracts;

import drawable.drawTool.DrawTool;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;


public abstract class DrawableRemoteCreature extends DrawableCreature {
    public DrawableRemoteCreature(DrawTool tool, LocalDrawSetting settings) {
        super(new Vector2D(0, 0), new Vector2D(0, 0), tool  , settings);
    }
}
