package model.objects.movingObject.trajectory;

import tools.Timer;
import tools.Vector2D;

public abstract class MoveFunction {


    public abstract boolean isReached(Vector2D position);

    public abstract Vector2D tickAndGet(double deltaTime, double speed, Vector2D position);

    public void parentPositionWasChangedBy(Vector2D diference) {
    }

    public static MoveFunction InDirectionAndTimer(Vector2D direction, long timeToDie) {
        return new MoveFunction() {
            Timer timer = new Timer(timeToDie);

            public Vector2D tickAndGet(double deltaTime, double speed, Vector2D startPosition) {
                timer.tick(deltaTime);
                return startPosition.getAdded(direction.getMultiplied(deltaTime * speed));
            }

            @Override
            public boolean isReached(Vector2D ignored) {
                return timer.isReady();
            }
        };
    }

    public static MoveFunction SetPosition(Vector2D positionToSet) {
        return new MoveFunction() {
            @Override
            public Vector2D tickAndGet(double deltaTime, double speed, Vector2D position) {
                return positionToSet;
            }

            @Override
            public boolean isReached(Vector2D ignored) {
                return false;
            }
        };
    }
        // TODO change to function getter
    public static MoveFunction GetToDestination(Vector2D destination) {
        return new MoveFunction() {
            final Vector2D destinationCopy = destination;

            @Override
            public boolean isReached(Vector2D position) {
                return position.equals(destinationCopy);
            }

            @Override
            public Vector2D tickAndGet(double deltaTime, double speed, Vector2D position) {
                return position.getShiftedByDistance(destinationCopy, deltaTime * speed);
            }

            public void parentPositionWasChangedBy(Vector2D diference) {
                destinationCopy.getAdded(diference);
            }
        };
    }

    public static MoveFunction Constant() {
        return new MoveFunction() {
            @Override
            public boolean isReached(Vector2D position) {
                return true;
            }

            @Override
            public Vector2D tickAndGet(double deltaTime, double speed, Vector2D position) {
                return position;
            }
        };
    }
}
