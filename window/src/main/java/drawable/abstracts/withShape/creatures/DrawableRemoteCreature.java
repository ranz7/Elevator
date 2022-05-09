package drawable.abstracts.withShape.creatures;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.objects.DrawableCreature;
import model.objects.Creature;

public abstract class DrawableRemoteCreature extends DrawableCreature {
    public DrawableRemoteCreature(Creature creature, CombienedDrawDataBase settings) {
        super(creature, settings);
    }

    public long getId() {
        return creature.getId();
    }

    public void set(Creature creature) {
        this.creature = creature;
    }
}
