package drawable.abstracts;

import settings.CombienedDrawSettings;
import drawable.drawTool.DrawTool;
import model.objects.Creature;
import tools.Vector2D;


public abstract class DrawableRemoteCreature extends DrawableCreature {
    public DrawableRemoteCreature(DrawTool tool, CombienedDrawSettings settings) {
        super(new Vector2D(0, 0), new Vector2D(0, 0), tool  , settings);
    }

    @Override
    public CombienedDrawSettings getSettings() {
        return (CombienedDrawSettings) super.getSettings();
    }

    public void set(Creature creature) {
        set(creature);
    }
}
