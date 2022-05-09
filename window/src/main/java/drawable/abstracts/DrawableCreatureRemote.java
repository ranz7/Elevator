package drawable.abstracts;

import configs.tools.CombienedDrawDataBase;
import drawable.drawTool.DrawTool;
import model.objects.Creature;

public abstract class DrawableCreatureRemote extends DrawableCreature {
    public DrawableCreatureRemote(Creature creature, DrawTool tool, CombienedDrawDataBase settings) {
        super(creature, tool, settings);
    }

    public long getId() {
        return super.getId();
    }

    public void set(Creature creature) {
        creature.set(creature);
    }
}
