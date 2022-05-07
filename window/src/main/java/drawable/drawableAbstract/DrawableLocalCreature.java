package drawable.drawableAbstract;

import configs.CanvasSettings.MainSettings;
import model.objects.Creature;
import tools.Vector2D;

public abstract class DrawableLocalCreature extends DrawableCreature {
    public DrawableLocalCreature(Vector2D position, Vector2D size, MainSettings settings) {
        super(new Creature(position, size), settings);
    }
}
