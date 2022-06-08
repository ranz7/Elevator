package model.objects.movingObject.trajectory;

import tools.Timer;
import tools.Vector2D;

import java.util.function.Supplier;

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
                return startPosition.add(direction.multiply(deltaTime * speed));
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

    public static MoveFunction GetToDestination(Supplier<Vector2D> destination) {
        return new MoveFunction() {

            @Override
            public boolean isReached(Vector2D position) {
                return position.equals(destination.get());
            }

            @Override
            public Vector2D tickAndGet(double deltaTime, double speed, Vector2D position) {
                return position.getShiftedByDistance(destination.get(), deltaTime * speed);
            }

            public void parentPositionWasChangedBy(Vector2D diference) {
                destination.get().add(diference);
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

    public static MoveFunction MoveByVector(Vector2D differenceVector) {
        return new MoveFunction() {
            private final Vector2D vector = differenceVector;

            @Override
            public boolean isReached(Vector2D position) {
                return vector.isZero();
            }

            @Override
            public Vector2D tickAndGet(double deltaTime, double speed, Vector2D position) {
                var newPosition = position.getShiftedByDistance(position.add(vector), deltaTime * speed);

                vector.set(vector.sub(newPosition.sub(position)));
                return newPosition;
            }
        };
    }

}
