package model.objects.customer;

import model.objects.movingObject.MovingObject;
import model.objects.elevator.Elevator;
import tools.Timer;
import tools.Vector2D;

import java.awt.*;


public class Customer extends MovingObject {
    private final int currentFlor;
    private Elevator currentElevator;
    private CustomerState state;

    public Timer MAIN_TIMER = new Timer();
    private final int FLOOR_TO_END;

    public Customer(int currentFlor, int floorEnd, Vector2D position, double speed, Point size) {
        super(position, speed, size);
        this.FLOOR_TO_END = floorEnd;
        this.currentFlor = currentFlor;
    }

    @Override
    public void tick(long deltaTime) {
        super.tick(deltaTime);
    }

}
