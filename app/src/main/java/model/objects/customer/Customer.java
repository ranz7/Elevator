package model.objects.customer;

import model.objects.movingObject.MovingObject;
import model.objects.elevator.Elevator;


public class Customer extends MovingObject {
    private final int FLOOR_TO_END = 0;
    private Elevator currentElevator;
    private CustomerState state;
}
