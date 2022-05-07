package drawable.drawableAbstract;

import configs.tools.CombienedDrawDataBase;
import model.objects.Creature;
import tools.Vector2D;

public abstract class DrawableLocalCreature extends DrawableCreature {
    public DrawableLocalCreature(Vector2D position, Vector2D size, CombienedDrawDataBase settings) {
        super(new Creature(position, size), settings);
    }
}
