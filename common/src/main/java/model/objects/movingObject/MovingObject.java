package model.objects.movingObject;


import architecture.tickable.Tickable;
import lombok.Getter;
import lombok.Setter;

import model.objects.Creature;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Vector2D;

public class MovingObject extends Creature implements Tickable {

    @Getter
    @Setter
    protected boolean isDead = false;
    private final Trajectory trajecotry;

    public MovingObject(Vector2D position, Vector2D size, Trajectory trajecotry) {
        super(position, size);
        this.trajecotry = trajecotry;
    }

    public void tick(long delta_time) {
        position = trajecotry.tickAndGet(delta_time, position);
    }

    public void setPosition(Vector2D newPosition) {
        position = newPosition;
    }

    public boolean isReachedDestination() {
        return trajecotry.reached(position);
    }

    public double getConstSpeed() {
        return trajecotry.getConstSpeed();
    }

    public void setMoveTrajectory(Trajectory trajectory) {
        trajecotry.set(trajectory);
    }
}