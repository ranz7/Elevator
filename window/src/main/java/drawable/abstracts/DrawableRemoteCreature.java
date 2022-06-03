package drawable.abstracts;

import drawable.drawTool.DrawTool;
import model.packageLoader.DrawableCreatureData;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;


public abstract class DrawableRemoteCreature extends DrawableCreature {
    public DrawableRemoteCreature(DrawableCreatureData data, DrawTool tool, LocalDrawSetting settings) {
        super(new Vector2D(0, 0), new Vector2D(0, 0), tool, settings);
        set(data);
    }
}
