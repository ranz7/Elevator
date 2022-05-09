package model.objects.customer;

import model.objects.movingObject.MovingObject;
import model.objects.elevator.Elevator;
import lombok.Setter;
import lombok.Getter;
import model.objects.movingObject.trajectory.SpeedFunction;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Timer;
import tools.Vector2D;

@Getter
public class Customer extends MovingObject {
    public Timer MAIN_TIMER = new Timer();
    private final int FLOOR_TO_END;
    @Setter
    private int currentFlor;
    private Elevator currentElevator;
    private double lastCurrentElevatorXPosition;
    @Setter
    private CustomerState state = CustomerState.GO_TO_BUTTON;

    public Customer(int currentFlor, int floorEnd, Vector2D position, double speed, Vector2D size) {
        super(position, size, new Trajectory().add(SpeedFunction.WithConstantSpeed(speed)));
        this.FLOOR_TO_END = floorEnd;
        this.currentFlor = currentFlor;
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
        MAIN_TIMER.tick(deltaTime);
        if (currentElevator != null) {
            position.y = currentElevator.getPosition().y;
            if (lastCurrentElevatorXPosition != currentElevator.getPosition().x) {
                position.x -= (lastCurrentElevatorXPosition - currentElevator.getPosition().x);
                lastCurrentElevatorXPosition = currentElevator.getPosition().x;
            }
            if (currentElevator.isInMotion()) {
                setCurrentFlor(currentElevator.getCurrentFloor());
                setVisible(false);
            } else {
                setVisible(true);
            }
            if (!currentElevator.isVisible()) {
                isDead = true;
            }
            return;
        }

        setVisible(true);
    }

    public void setCurrentElevator(Elevator currentElevator) {
        this.currentElevator = currentElevator;
        if (currentElevator != null) {
            lastCurrentElevatorXPosition = currentElevator.getPosition().x;
        }
    }

    public boolean wantsGoUp() {
        return currentFlor < FLOOR_TO_END;
    }

}
