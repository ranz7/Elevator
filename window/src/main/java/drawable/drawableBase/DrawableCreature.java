package drawable.drawableBase;

import configs.CanvasSettings.MainSettings;
import model.objects.Creature;
import tools.Vector2D;

public abstract class DrawableCreature extends Drawable {
    private final Creature creature;

    public DrawableCreature(Vector2D position, Vector2D size, MainSettings settings) {
        super(settings);
        creature = new Creature(position, size);
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

}
