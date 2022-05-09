package drawable.abstracts.withShape.creatures;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.objects.DrawableCreature;
import drawable.drawTool.figuresComponent.FiguresComponent;
import model.objects.Creature;
import tools.Vector2D;

public abstract class DrawableLocalCreature extends DrawableCreature {
    public DrawableLocalCreature(Vector2D position, Vector2D size, FiguresComponent component, CombienedDrawDataBase settings) {
        super(new Creature(position, size),component, settings);
    }
}
