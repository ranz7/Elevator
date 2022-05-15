package model.objects.movingObject;


import architecture.tickable.Tickable;
import lombok.Getter;
import lombok.Setter;

import model.objects.Creature;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Vector2D;

public class MovingCreature extends Creature implements Tickable {
    private final Trajectory trajecotry;

    public MovingCreature(Vector2D position, Vector2D size, Trajectory trajecotry) {
        super(position, size);
        this.trajecotry = trajecotry;
    }

    @Getter
    @Setter
    protected boolean isDead = false;

    @Override
    public void tick(double delta_time) {
        position = trajecotry.tickAndGet(delta_time, getPosition());
    }

    public boolean isReachedDestination() {
        return trajecotry.reached(getPosition());
    }

    public double getConstSpeed() {
        return trajecotry.getConstSpeed();
    }

    public void setMoveTrajectory(Trajectory trajectory) {
        trajecotry.add(trajectory);
    }
}