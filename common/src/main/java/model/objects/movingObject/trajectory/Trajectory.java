package model.objects.movingObject.trajectory;

import tools.Vector2D;

import java.util.function.Supplier;

public class Trajectory {
    protected final double speedConstant = 1000;
    private SpeedFunction speedFunction;
    private MoveFunction moveFunction;

    public static Trajectory ConstantSpeedInDirectionWithTimer(Vector2D direction, long time, double speed) {
        return new Trajectory()
                .add(SpeedFunction.WithConstantSpeed(()->speed))
                .add(MoveFunction.InDirectionAndTimer(direction, time));
    }

    public static Trajectory MomentarilyPositionChange(Vector2D position) {
        return new Trajectory().add(SpeedFunction.Momentarily()).add(MoveFunction.SetPosition(position));
    }

    public static Trajectory WithOldSpeedToTheDestination(Supplier<Vector2D> destination) {
        return new Trajectory().add(MoveFunction.GetToDestination(destination));
    }

    public static Trajectory ToTheDestination(double constSpeed, Supplier<Vector2D> destination) {
        return new Trajectory().add(MoveFunction.GetToDestination(destination))
                .add(SpeedFunction.WithConstantSpeed(()->constSpeed));
    }


    public static Trajectory WithOldSpeedByVector(Vector2D differenceVector) {
        return new Trajectory().add(MoveFunction.MoveByVector(differenceVector));
    }

    public static Trajectory StaticPoint() {
        return new Trajectory().add(MoveFunction.Constant()).add(SpeedFunction.WithConstantSpeed(()->0.));
    }

    public static Trajectory StayOnPlaceWithConstSpeed(double speed) {
        return new Trajectory().add(MoveFunction.Constant()).add(SpeedFunction.WithConstantSpeed(()->speed));
    }

    public static Trajectory StayInPlaceWithSpeedSupplier(Supplier<Double> speedFunction) {
        return new Trajectory().add(MoveFunction.Constant()).add(SpeedFunction.WithConstantSpeed(speedFunction));
    }
    public static Trajectory WithOldSpeedToTheDestination(Vector2D destination) {
        return new Trajectory().add(MoveFunction.GetToDestination(()->destination));
    }

    public Vector2D tickAndGet(double deltaTime, Vector2D startPosition) {
        if (!initialized()) {
            throw new RuntimeException("Trajectory need to have sett Function and Speed");
        }
        return moveFunction.tickAndGet(deltaTime, speedFunction.tickAndGet(deltaTime) / speedConstant, startPosition);
    }

    private boolean initialized() {
        return moveFunction != null && speedFunction != null;
    }

    public boolean reached(Vector2D position) {
        return moveFunction.isReached(position);
    }

    public double getSpeed() {
        return speedFunction.tickAndGet(0);
    }

    public Trajectory add(Trajectory trajectory) {
        if (trajectory.speedFunction != null) {
            add(trajectory.speedFunction);
        }
        if (trajectory.moveFunction != null) {
            add(trajectory.moveFunction);
        }
        return this;
    }

    public Trajectory add(SpeedFunction speedFunction) {
        this.speedFunction = speedFunction;
        return this;
    }

    public Trajectory add(MoveFunction moveFunction) {
        this.moveFunction = moveFunction;
        return this;
    }

    public Trajectory WithNewConstSpeedToOldDestination(double speed) {
        return new Trajectory().add(SpeedFunction.WithConstantSpeed(()->speed));
    }

    public void setSpeedCoefficient(Double coefficient) {
        speedFunction.setSpeedCoefficient(coefficient);
    }
}
