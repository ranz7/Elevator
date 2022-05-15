package drawable.abstracts;

import databases.CombienedDrawDataBase;
import drawable.drawTool.DrawTool;
import model.objects.movingObject.MovingCreature;

public abstract class DrawableMovingCreature extends DrawableCreature {
    public DrawableMovingCreature(MovingCreature movingCreature, DrawTool drawTool, CombienedDrawDataBase settings) {
        super(movingCreature, drawTool, settings);
    }

    @Override
    public void tick(double deltaTime) {
        ((MovingCreature) creature).tick(deltaTime);
    }

    public final boolean isDead() {
        return ((MovingCreature) creature).isReachedDestination();
    }

}
