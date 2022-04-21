package model.objects.elevator;

import model.objects.movingObject.MovingObject;

/*
 * Elevator is storing all requests under and behind his way, the algorithm finds the closest floor by
 * calculating distance to came from floor A to floor B and all intermediate floors.
 */
public class Elevator extends MovingObject {
    private final long TIME_TO_STOP_ON_FLOOR = 0;
    private final int MAX_HUMAN_CAPACITY = 0;


    private ElevatorState state;
}
