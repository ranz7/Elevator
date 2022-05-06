package controller;

import configs.CustomerSettings;
import lombok.Getter;
import architecture.tickable.Tickable;
import tools.Vector2D;
import model.AppModel;
import model.objects.elevator.ElevatorRequest;
import model.objects.customer.CustomerState;
import model.objects.customer.Customer;
import connector.protocol.Protocol;

import java.util.Random;

import tools.Timer;

/**
 * Manipulate all customers in game.
 *
 * @see CustomerSettings
 */
public class CustomersConductor implements Tickable {
    @Getter
    private final CustomerSettings settings = new CustomerSettings();
    private final ElevatorsConductor ELEVATOR_SYSTEM_CONTROLLER;
    private final Timer spawnTimer = new Timer();
    private final AppController AppCONTROLLER;
    AppModel appModel;

    public CustomersConductor(AppController appController) {
        this.ELEVATOR_SYSTEM_CONTROLLER = appController.elevatorsConductor;
        this.AppCONTROLLER = appController;
    }

    @Override
    public void tick(long deltaTime) {
        spawnTimer.tick(deltaTime);

        if (spawnTimer.isReady() && AppCONTROLLER.appModel.customers.size() < settings.MAX_CUSTOMERS) {
            var maxFloor = ELEVATOR_SYSTEM_CONTROLLER.getSettings().FLOORS_COUNT;
            var randomStartFloor = new Random().nextInt(0, maxFloor);
            var randomEndFloor = new Random().nextInt(0, maxFloor);
            randomEndFloor = (maxFloor + randomStartFloor + randomEndFloor - 1) % maxFloor;
            CreateCustomer(randomStartFloor, randomEndFloor);

            spawnTimer.restart(settings.SPAWN_RATE);
        }

        for (var customer : AppCONTROLLER.appModel.customers) {
            switch (customer.getState()) {
                case GO_TO_BUTTON -> processGoToButton(customer);
                case WAIT_UNTIL_ARRIVED -> processWaitUntilArrived(customer);
                case GET_IN -> processGetIn(customer);
                case STAY_IN -> processStayIn(customer);
                case GET_OUT -> processGetOut(customer);
            }
        }
    }

    private void processGoToButton(Customer customer) {
        var buttonPosition = AppCONTROLLER.appModel.getBuilding()
                .getClosestButtonOnFloor(customer.getPosition());
        customer.setMoveTrajectory(buttonPosition);
        if (customer.isReachedDestination()) {
            ELEVATOR_SYSTEM_CONTROLLER.buttonClick(new ElevatorRequest(customer.getPosition(), customer.wantsGoUp()));
            customer.MAIN_TIMER.restart(settings.TIME_TO_WAIT_AFTER_BUTTON_CLICK);
            customer.setState(CustomerState.WAIT_UNTIL_ARRIVED);
            customer.setSpeedMultiPly(settings.SLOW_SPEED_MULTIPLY);
        }
    }

    private void processWaitUntilArrived(Customer customer) {
        var nearestOpenedElevatorOnFloor = AppCONTROLLER.appModel.getBuilding()
                .getClosestOpenedElevatorOnFloor(customer.getPosition(), customer.getCurrentFlor());
        if (nearestOpenedElevatorOnFloor != null) {
            customer.setMoveTrajectory(nearestOpenedElevatorOnFloor.getPosition());
            customer.setSpeedMultiPly(settings.FAST_SPEED_MULTIPLY);
            customer.setState(CustomerState.GET_IN);
            return;
        }
        if (customer.getMAIN_TIMER().isReady()) {
            var getPositionToWalk = new Vector2D(
                    new Random().nextInt(0, (int) ELEVATOR_SYSTEM_CONTROLLER.getSettings().BUILDING_SIZE.x),
                    customer.getPosition().y);
            customer.setSpeedMultiPly(settings.SLOW_SPEED_MULTIPLY);
            customer.getMAIN_TIMER().restart(settings.TIME_TO_WALK);
            customer.setMoveTrajectory(getPositionToWalk);
        }
    }

    private void processGetIn(Customer customer) {
        var closestOpenedElevator = AppCONTROLLER.appModel.getBuilding()
                .getClosestOpenedElevatorOnFloor(customer.getPosition(), customer.getCurrentFlor());
        if (closestOpenedElevator == null) {
            customer.setState(CustomerState.GO_TO_BUTTON);
            customer.setSpeedMultiPly(1);
            return;
        }
        customer.setMoveTrajectory(closestOpenedElevator.getPosition());

        if (customer.isReachedDestination()) {
            ELEVATOR_SYSTEM_CONTROLLER.getCustomerIntoElevator(closestOpenedElevator);
            AppCONTROLLER.Send(Protocol.CUSTOMER_GET_IN_OUT, customer.getId());
            customer.setCurrentElevator(closestOpenedElevator);

            var makeSpaceInElevator = ELEVATOR_SYSTEM_CONTROLLER.getSettings().ELEVATOR_SIZE.x / 2;
            var newDestination = new Vector2D(
                    new Random().nextDouble(
                            -makeSpaceInElevator,
                            makeSpaceInElevator - customer.getSize().x), 0);
            ELEVATOR_SYSTEM_CONTROLLER.setFloorToReach(customer.getCurrentElevator(), customer.getFLOOR_TO_END());
            customer.setMoveTrajectory(customer.getPosition().getAdded(newDestination));
            customer.setState(CustomerState.STAY_IN);
            customer.setSpeedMultiPly(1);
        }
    }

    private void processStayIn(Customer customer) {
        if (customer.getCurrentElevator().isOpened()) {
            if (customer.getCurrentFlor() == customer.getFLOOR_TO_END()) {
                ELEVATOR_SYSTEM_CONTROLLER.getOutFromElevator(customer.getCurrentElevator());
                customer.setMoveTrajectory(customer.getCurrentElevator().getPosition());
                customer.setState(CustomerState.GET_OUT);
                customer.setCurrentElevator(null);
            }
        }
    }

    private void processGetOut(Customer customer) {
        if (!customer.isReachedDestination()) {
            return;
        }
        if (customer.getPosition().x > ELEVATOR_SYSTEM_CONTROLLER.getSettings().BUILDING_SIZE.x + customer.getSize().x ||
                customer.getPosition().x < -customer.getSize().x) {
            customer.setDead(true);
            return;
        }
        customer.setMoveTrajectory(getStartPositionForCustomer(customer.getCurrentFlor()));
        AppCONTROLLER.Send(Protocol.CUSTOMER_GET_IN_OUT, customer.getId());
    }

    public void CreateCustomer(int floorStart, int floorEnd) {
        double speed = new Random().nextDouble(
                settings.CUSTOMER_SPEED
                        - settings.CUSTOMER_SPEED / 3,
                settings.CUSTOMER_SPEED
                        + settings.CUSTOMER_SPEED / 3);
        var startPosition = getStartPositionForCustomer(floorStart);
        var customer = new Customer(floorStart, floorEnd, startPosition, speed,
                settings.CUSTOMER_SIZE);

        customer.setState(CustomerState.GO_TO_BUTTON);
        AppCONTROLLER.appModel.customers.add(customer);
    }

    private Vector2D getStartPositionForCustomer(int floorStart) {
        Vector2D startPosition = AppCONTROLLER.appModel.getBuilding().getStartPositionAfterBuilding(floorStart);
        // So u can't see customer when he spawns
        if (startPosition.x == 0) {
            startPosition.x -= settings.CUSTOMER_SIZE.x * 2;
        } else {
            startPosition.x += settings.CUSTOMER_SIZE.x * 2;
        }
        return startPosition;
    }

    public void setModel(AppModel appModel) {
        this.appModel = appModel;
    }
}
