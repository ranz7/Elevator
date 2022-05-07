package drawable.drawableAbstract;

import configs.CanvasSettings.MainSettings;
import model.objects.movingObject.MovingObject;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Vector2D;

public abstract class DrawableLocalMoving extends Drawable {
    private final MovingObject movingObject;

    public DrawableLocalMoving(Vector2D position, Vector2D size, Trajectory trajecotry, MainSettings settings) {
        super(settings);
        this.movingObject = new MovingObject(position, size, trajecotry);
    }

    public DrawableLocalMoving(Vector2D position, double size, Trajectory trajecotry, MainSettings settings) {
        super(settings);
        this.movingObject = new MovingObject(position, new Vector2D(size, size), trajecotry);
    }

    @Override
    public void tick(long deltaTime) {
        movingObject.tick(deltaTime);
    }

    public Vector2D getPosition() {
        return movingObject.getPosition();
    }

    public Vector2D getSize() {
        return movingObject.getSize();
    }

    public boolean getIsVisible() {
        return movingObject.isVisible();
    }

    public boolean isReachedDestination() {
        return movingObject.isReachedDestination();
    }

    public abstract boolean isDead();
}
