package model.objects.movingObject.trajectory;

import tools.Vector2D;

public class Trajectory {

    private SpeedFunction speedFunction;
    private MoveFunction moveFunction;

    public static Trajectory ConstantSpeedInDirectionWithTimer(Vector2D direction, long time, int speed) {
        return new Trajectory()
                .set(SpeedFunction.WithConstantSpeed(speed))
                .set(MoveFunction.InDirectionAndTimer(direction, time));
    }

    public static Trajectory MomentarilyPositionChange(Vector2D position) {
        return new Trajectory().set(SpeedFunction.Momentarily()).set(MoveFunction.SetPosition(position));
    }

    public static Trajectory WithOldSpeedToTheDestination(Vector2D destination) {
        return new Trajectory().set(MoveFunction.GetToDestination(destination));
    }

    public static Trajectory ToTheDestination(double constSpeed, Vector2D position) {
        return new Trajectory().set(MoveFunction.GetToDestination(position))
                .set(SpeedFunction.WithConstantSpeed(constSpeed));
    }

    public Vector2D tickAndGet(long deltaTime) {
        if (!initialized()) {
            throw new RuntimeException("Trajectory need to have sett Function and Speed");
        }

        var oldPos = moveFunction.getPosition();
        moveFunction.tick(deltaTime);
        return moveFunction.getPosition().getSubbed(oldPos);
    }

    private boolean initialized() {
        return moveFunction != null && speedFunction != null;
    }

    public boolean reached() {
        return moveFunction.isReached();
    }

    public double getConstSpeed() {
        if (!speedFunction.isConstFunction()) {
            throw new RuntimeException("Try to get constant speed from non constant function.");
        }
        return speedFunction.getSpeed();
    }

    public Trajectory set(Trajectory trajectory) {
        if (trajectory.speedFunction != null) {
            set(trajectory.speedFunction);
        }
        if (trajectory.moveFunction != null) {
            set(trajectory.moveFunction);
        }
        return this;
    }

    public Trajectory set(SpeedFunction speedFunction) {
        this.speedFunction = speedFunction;
        return this;
    }

    public Trajectory set(MoveFunction moveFunction) {
        this.moveFunction = moveFunction;
        return this;
    }
}
