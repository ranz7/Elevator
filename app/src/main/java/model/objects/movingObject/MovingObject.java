package model.objects.movingObject;


import lombok.Getter;
import lombok.Setter;

import tools.Vector2D;
import java.awt.*;

public class MovingObject extends Creature {
    protected static final int SPEED_COEFFICIENT = 1000;
    private final double SPEED;

    @Getter
    @Setter
    private double speedMultiPly = 1;
    @Getter
    @Setter
    protected boolean isDead = false;
    protected Vector2D destination;

    public MovingObject(Vector2D position, double speed, Point size) {
        super(position, size);
        this.destination = position;
        this.SPEED = speed;
    }

    public MovingObject(Vector2D position, double speed) {
        super(position);
        this.destination = position;
        this.SPEED = speed;
    }

    public void tick(long delta_time) {
        if (!isReachedDestination()) {
            position = position.getShiftedByDistance(destination, delta_time * getSpeed() / SPEED_COEFFICIENT);
        }
    }
    public void setPosition(Vector2D newPosition) {
        destination = destination.getAdded(newPosition.getSubbed(position));
        position = newPosition;
    }

    public boolean isReachedDestination() {
        return new Vector2D(this.destination).getVectorTo(this.position).getLength() < Vector2D.EPSILON;
    }

    public double getSpeed() {
        return SPEED * speedMultiPly;
    }

    public void setDestination(Vector2D destination) {
        this.destination = destination;
    }


}