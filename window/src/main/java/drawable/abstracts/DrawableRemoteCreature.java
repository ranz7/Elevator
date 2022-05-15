package drawable.abstracts;

import databases.CombienedDrawDataBase;
import drawable.drawTool.DrawTool;
import model.objects.Creature;

public abstract class DrawableRemoteCreature extends DrawableCreature {
    public DrawableRemoteCreature(Creature creature, DrawTool tool, CombienedDrawDataBase settings) {
        super(creature, tool, settings);
    }

    public long getId() {
        return super.getId();
    }

    public void set(Creature creature) {
        this.creature.set(creature);
    }
}
