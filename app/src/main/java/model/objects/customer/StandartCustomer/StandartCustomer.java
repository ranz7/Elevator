package model.objects.customer.StandartCustomer;

import databases.configs.CustomerConfig;
import connector.protocol.Protocol;
import lombok.Setter;
import model.objects.customer.Customer;
import model.objects.elevator.Elevator;
import model.objects.elevator.ElevatorRequest;
import model.objects.movingObject.trajectory.SpeedFunction;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Timer;
import tools.Vector2D;

import java.util.Random;

import static model.objects.customer.StandartCustomer.StandartCustomerState.GO_TO_BUTTON;

public class StandartCustomer extends Customer {
    private final int FLOOR_TO_END;
    @Setter
    private int currentFlor;

    private double lastCurrentElevatorXPosition;
    private Elevator currentElevator;
    public Timer MAIN_TIMER = new Timer();

    @Setter
    private StandartCustomerState state = GO_TO_BUTTON;


    public StandartCustomer(int currentFlor, int floorEnd, Vector2D position, CustomerConfig settings) {
        super(position, size, speed);
        currentFlor = currentFlor;
        FLOOR_TO_END = floorEnd;
    }

    public Customer(int currentFlor, int floorEnd, Vector2D position, double speed, Vector2D size) {
        super(position, size, new Trajectory().add(SpeedFunction.WithConstantSpeed(speed)));
        this.FLOOR_TO_END = floorEnd;
        this.currentFlor = currentFlor;
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
        switch (state) {
            case GO_TO_BUTTON -> processGoToButton(customer);
            case WAIT_UNTIL_ARRIVED -> processWaitUntilArrived(customer);
            case GET_IN -> processGetIn(customer);
            case STAY_IN -> processStayIn(customer);
            case GET_OUT -> processGetOut(customer);
        }

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

    private void processGoToButton(Customer customer) {
        var buttonPosition = appController.appModel.getBuilding()
                .getClosestButtonOnFloor(customer.getPosition());
        customer.setMoveTrajectory(Trajectory.WithOldSpeedToTheDestination(buttonPosition));
        if (customer.isReachedDestination()) {
            ELEVATOR_SYSTEM_CONTROLLER.buttonClick(new ElevatorRequest(customer.getPosition(), customer.wantsGoUp()));
            customer.MAIN_TIMER.restart(settings.timeToWaitAfterButtonClick);
            customer.setState(StandartCustomerState.WAIT_UNTIL_ARRIVED);
        }
    }

    private void processWaitUntilArrived(Customer customer) {
        var nearestOpenedElevatorOnFloor = appController.appModel.getBuilding()
                .getClosestOpenedElevatorOnFloor(customer.getPosition(), customer.getCurrentFlor());
        if (nearestOpenedElevatorOnFloor != null) {
            customer.setMoveTrajectory(Trajectory.ToTheDestination(
                    customer.getConstSpeed(), // settings.FAST_SPEED_MULTIPLY
                    nearestOpenedElevatorOnFloor.getPosition()));
            customer.setState(StandartCustomerState.GET_IN);
            return;
        }
        if (customer.getMAIN_TIMER().isReady()) {
            var getPositionToWalk = new Vector2D(
                    new Random().nextInt(0, (int) ELEVATOR_SYSTEM_CONTROLLER.getSettings().buildingSize.x),
                    customer.getPosition().y);
            customer.setMoveTrajectory(Trajectory.ToTheDestination(
                    customer.getConstSpeed(), // * settings.SLOW_SPEED_MULTIPLY
                    getPositionToWalk));
            customer.getMAIN_TIMER().restart(settings.timeToWalk);

        }
    }

    private void processGetIn(Customer customer) {
        var closestOpenedElevator = appController.appModel.getBuilding()
                .getClosestOpenedElevatorOnFloor(customer.getPosition(), customer.getCurrentFlor());
        if (closestOpenedElevator == null) {
            customer.setState(GO_TO_BUTTON);
            customer.setMoveTrajectory(
                    new Trajectory().WithNewConstSpeedToOldDestination(customer.getConstSpeed()));
            return;
        }

        if (customer.isReachedDestination()) {
            ELEVATOR_SYSTEM_CONTROLLER.getCustomerIntoElevator(closestOpenedElevator);
            appController.Send(Protocol.CUSTOMER_GET_IN_OUT, customer.getId());
            customer.setCurrentElevator(closestOpenedElevator);

            var makeSpaceInElevator = ELEVATOR_SYSTEM_CONTROLLER.getSettings().elevatorSize.x / 2;
            var newDestination = new Vector2D(
                    new Random().nextDouble(
                            -makeSpaceInElevator,
                            makeSpaceInElevator - customer.getSize().x), 0);
            ELEVATOR_SYSTEM_CONTROLLER.setFloorToReach(customer.getCurrentElevator(), customer.getFLOOR_TO_END());
            var shiftToMakeSpaceInElevator = customer.getPosition().getAdded(newDestination);
            customer.setMoveTrajectory(Trajectory.ToTheDestination(
                    customer.getConstSpeed(), // * 1
                    shiftToMakeSpaceInElevator));
            customer.setState(StandartCustomerState.STAY_IN);
        }
    }

    private void processStayIn(Customer customer) {
        if (customer.getCurrentElevator().isOpened()) {
            if (customer.getCurrentFlor() == customer.getFLOOR_TO_END()) {
                ELEVATOR_SYSTEM_CONTROLLER.getOutFromElevator(customer.getCurrentElevator());
                customer.setMoveTrajectory(
                        Trajectory.WithOldSpeedToTheDestination(
                                customer.getCurrentElevator().getPosition()));
                customer.setState(StandartCustomerState.GET_OUT);
                customer.setCurrentElevator(null);
            }
        }
    }

    private void processGetOut(Customer customer) {
        if (!customer.isReachedDestination()) {
            return;
        }
        if (customer.getPosition().x > ELEVATOR_SYSTEM_CONTROLLER.getSettings().buildingSize.x + customer.getSize().x ||
                customer.getPosition().x < -customer.getSize().x) {
            customer.setDead(true);
            return;
        }
        customer.setMoveTrajectory(
                Trajectory.WithOldSpeedToTheDestination(
                        getStartPositionForCustomer(customer.getCurrentFlor())));
        appController.Send(Protocol.CUSTOMER_GET_IN_OUT, customer.getId());
    }

}
