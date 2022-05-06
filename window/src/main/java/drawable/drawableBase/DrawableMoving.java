package drawable.drawableBase;

import configs.CanvasSettings.MainSettings;
import model.objects.movingObject.MovingObject;
import tools.Vector2D;

public abstract class DrawableMoving extends Drawable {
    private final MovingObject movingObject;

    public DrawableMoving(Vector2D position, Vector2D size, Trajectory trajecotry, MainSettings settings) {
        super(settings);
        this.movingObject = new MovingObject(position, size, trajecotry);
    }

    public DrawableMoving(Vector2D position, double size, Trajectory trajecotry, MainSettings settings) {
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
}
