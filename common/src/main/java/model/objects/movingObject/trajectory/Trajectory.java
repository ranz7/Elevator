package model.objects.movingObject.trajectory;

import tools.Vector2D;

public class Trajectory {
    protected final double speedCoefficient = 1000;
    private SpeedFunction speedFunction;
    private MoveFunction moveFunction;

    public static Trajectory ConstantSpeedInDirectionWithTimer(Vector2D direction, long time, int speed) {
        return new Trajectory()
                .add(SpeedFunction.WithConstantSpeed(speed))
                .add(MoveFunction.InDirectionAndTimer(direction, time));
    }

    public static Trajectory MomentarilyPositionChange(Vector2D position) {
        return new Trajectory().add(SpeedFunction.Momentarily()).add(MoveFunction.SetPosition(position));
    }

    public static Trajectory WithOldSpeedToTheDestination(Vector2D destination) {
        return new Trajectory().add(MoveFunction.GetToDestination(destination));
    }

    public static Trajectory ToTheDestination(double constSpeed, Vector2D position) {
        return new Trajectory().add(MoveFunction.GetToDestination(position))
                .add(SpeedFunction.WithConstantSpeed(constSpeed));
    }

    public static Trajectory StaticPoint() {
        return new Trajectory().add(MoveFunction.Constant()).add(SpeedFunction.WithConstantSpeed(0));
    }

    public static Trajectory StayOnPlaceWithDefaultConstantSpeed(double elevatorSpeed) {
        return new Trajectory().add(MoveFunction.Constant()).add(SpeedFunction.WithConstantSpeed(elevatorSpeed));
    }

    public Vector2D tickAndGet(double deltaTime, Vector2D startPosition) {
        if (!initialized()) {
            throw new RuntimeException("Trajectory need to have sett Function and Speed");
        }
        return moveFunction.tickAndGet(deltaTime, speedFunction.tickAndGet(deltaTime) / speedCoefficient, startPosition);
    }

    private boolean initialized() {
        return moveFunction != null && speedFunction != null;
    }

    public boolean reached(Vector2D position) {
        return moveFunction.isReached(position);
    }

    public double getConstSpeed() {
        if (!speedFunction.isConstFunction()) {
            throw new RuntimeException("Try to get constant speed from non constant function.");
        }
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
        return new Trajectory().add(SpeedFunction.WithConstantSpeed(speed));
    }
}
