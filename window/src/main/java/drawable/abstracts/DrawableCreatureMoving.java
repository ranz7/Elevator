package drawable.abstracts;

import configs.tools.CombienedDrawDataBase;
import drawable.drawTool.DrawTool;
import model.objects.movingObject.MovingObject;

public abstract class DrawableCreatureMoving extends DrawableCreature {
    public DrawableCreatureMoving(MovingObject movingObject, DrawTool drawTool, CombienedDrawDataBase settings) {
        super(movingObject, drawTool, settings);
    }

    @Override
    public void tick(double deltaTime) {
        ((MovingObject) creature).tick(deltaTime);
    }

    public boolean isReachedDestination() {
        return ((MovingObject) creature).isReachedDestination();
    }
}
