package drawable.abstracts;

import settings.CombienedDrawSettings;
import drawable.drawTool.DrawTool;
import model.objects.Creature;
import tools.Vector2D;

public abstract class DrawableRemoteCreature extends DrawableCreature {
    public DrawableRemoteCreature(Vector2D position, Vector2D size, DrawTool tool, CombienedDrawSettings settings) {
        super(position, size, tool, settings);
    }

    @Override
    public CombienedDrawSettings getSettings() {
        return (CombienedDrawSettings) super.getSettings();
    }

    public long getId() {
        return id;
    }

    public void set(Creature creature) {
        set(creature);
    }

}
