package drawable.abstracts.objects;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.Drawable;
import model.objects.movingObject.MovingObject;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Vector2D;

public abstract class DrawableMoving extends Drawable {
    private final MovingObject movingObject;

    public DrawableMoving(Vector2D position, Vector2D size, Trajectory trajecotry, CombienedDrawDataBase settings) {
        super(settings);
        this.movingObject = new MovingObject(position, size, trajecotry);
    }

    public DrawableMoving(Vector2D position, double size, Trajectory trajecotry, CombienedDrawDataBase settings) {
        super(settings);
        this.movingObject = new MovingObject(position, new Vector2D(size, size), trajecotry);
    }

    @Override
    public void tick(double deltaTime) {
        movingObject.tick(deltaTime);
    }

    public Vector2D getPosition() {
        return movingObject.getPosition();
    }

    public Vector2D getSize() {
        return movingObject.getSize();
    }

    public void setSize(Vector2D newSize) {
        movingObject.setSize(newSize);
    }

    public boolean getIsVisible() {
        return movingObject.isVisible();
    }

    public boolean isReachedDestination() {
        return movingObject.isReachedDestination();
    }

}
