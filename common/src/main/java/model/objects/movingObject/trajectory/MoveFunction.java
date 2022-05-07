package model.objects.movingObject.trajectory;

import architecture.tickable.Tickable;
import tools.Timer;
import tools.Vector2D;

public abstract class MoveFunction implements Tickable {
    protected final double speedCoefficient = 1000;

    protected Vector2D position;

    /**
     * get Current position of function
     */
    public abstract Vector2D getPosition();

    public abstract boolean isReached();

    public static MoveFunction InDirectionAndTimer(Vector2D direction, long timeToDie) {
        return new MoveFunction() {
            Timer timer = new Timer(timeToDie);

            @Override
            public void tick(long deltaTime) {
                timer.tick(deltaTime);
                position.getAdded(direction.getMultiplied(deltaTime / speedCoefficient));
            }

            @Override
            public Vector2D getPosition() {
                return position;
            }

            @Override
            public boolean isReached() {
                return timer.isReady();
            }

        };
    }

    public static MoveFunction SetPosition(Vector2D positionToSet) {
        return new MoveFunction() {

            @Override
            public Vector2D getPosition() {
                return positionToSet;
            }

            @Override
            public boolean isReached() {
                return true;
            }

            @Override
            public void tick(long deltaTime) {
            }
        };
    }

    public static MoveFunction GetToDestination(Vector2D destinationTmp) {
        return new MoveFunction() {
            Vector2D destination = new Vector2D(destinationTmp);
            @Override
            public Vector2D getPosition() {
                return position;
            }

            @Override
            public boolean isReached() {
                return position.equals(destination);
            }

            @Override
            public void tick(long deltaTime) {

            }
        };
    }
}
