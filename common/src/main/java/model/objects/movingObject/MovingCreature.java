package model.objects.movingObject;


import model.objects.Creature;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Vector2D;

public class MovingCreature extends Creature  {
    private final Trajectory trajecotry;

    public MovingCreature(Vector2D position, Vector2D size, Trajectory trajecotry) {
        super(position, size);
        this.trajecotry = trajecotry;
    }

    protected void setSpeedCoefficient(Double coefficient) {
        trajecotry.setSpeedCoefficient(coefficient);
    }

    @Override
    public void tick(double delta_time) {
        position = trajecotry.tickAndGet(delta_time, getPosition());
    }

    public boolean isReachedDestination() {
        return trajecotry.reached(getPosition());
    }

    public double getSpeed() {
        return trajecotry.getSpeed();
    }

    public void setMoveTrajectory(Trajectory trajectory) {
        trajecotry.add(trajectory);
    }
}