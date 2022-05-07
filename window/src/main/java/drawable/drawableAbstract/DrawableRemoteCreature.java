package drawable.drawableAbstract;

import configs.CanvasSettings.MainSettings;
import model.objects.Creature;

public abstract class DrawableRemoteCreature extends DrawableCreature {
    public DrawableRemoteCreature(Creature creature, MainSettings settings) {
        super(creature, settings);
    }

    public long getId() {
        return creature.getId();
    }

    public void set(Creature creature) {
        this.creature = creature;
    }
}
