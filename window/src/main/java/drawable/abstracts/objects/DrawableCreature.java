package drawable.abstracts.objects;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.Drawable;
import model.objects.Creature;
import tools.Vector2D;

public abstract class DrawableCreature extends Drawable {
    protected DrawableCreature(Creature creature,  CombienedDrawDataBase settings) {
        super(settings);
        this.creature = creature;
    }

    public Vector2D getPosition() {
        return creature.getPosition();
    }

    public Vector2D getSize() {
        return creature.getSize();
    }

    public boolean getIsVisible() {
        return creature.isVisible();
    }

    protected Creature creature;
}
