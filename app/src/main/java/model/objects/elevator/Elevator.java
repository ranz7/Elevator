package model.objects.elevator;

import model.objects.movingObject.MovingObject;
import tools.Vector2D;

import java.awt.*;

/*
 * Elevator is storing all requests under and behind his way, the algorithm finds the closest floor by
 * calculating distance to came from floor A to floor B and all intermediate floors.
 */
public class Elevator extends MovingObject {
    private final long TIME_TO_STOP_ON_FLOOR = 0;
    private final int MAX_HUMAN_CAPACITY = 0;

    public Elevator() {
        super(new Vector2D(0, 0),0,new Point(10,10));
        this.state = ElevatorState.OPENING;
    }


    private ElevatorState state;
}
