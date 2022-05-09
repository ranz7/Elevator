package drawable.abstracts.withShape.moving;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.objects.DrawableMoving;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Vector2D;

public abstract class DrawableLocalMoving extends DrawableMoving {
    public DrawableLocalMoving(Vector2D position, Vector2D size, Trajectory trajecotry, CombienedDrawDataBase settings) {
        super(position, size, trajecotry, settings);
    }

    public DrawableLocalMoving(Vector2D position, double size, Trajectory trajecotry, CombienedDrawDataBase settings) {
        super(position, size, trajecotry, settings);
    }

    public abstract boolean isDead();
}
