package model.objects.movingObject;


import lombok.Getter;
import lombok.Setter;
import tools.Vector2D;

import java.awt.*;

public class MovingObject extends Creature {
    protected static final int SPEED_COEFFICIENT = 1000;
    private final double SPEED;
    private final Vector2D destination;

    @Getter
    @Setter
    private double speedMultiPly = 1;
    @Getter
    @Setter
    protected boolean isDead = false;
    public MovingObject(Vector2D position, double speed, Point size) {
        super(position, size);
        this.destination = position;
        this.SPEED = speed;
    }

    public void tick(long delta_time) {

    }
    public MovingObject(Vector2D position, double speed) {
        super(position);
        this.destination = position;
        this.SPEED = speed;
    }

}