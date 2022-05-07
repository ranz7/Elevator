package drawable.drawableAbstract;

import configs.CanvasSettings.MainSettings;
import model.objects.Creature;
import tools.Vector2D;

abstract class DrawableCreature extends Drawable {
    protected DrawableCreature(Creature creature, MainSettings settings) {
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
