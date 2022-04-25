package controller.customerController;

import controller.elevatorSystemController.ElevatorSystemController;
import model.objects.elevator.ElevatorRequest;
import model.objects.customer.CustomerState;
import model.objects.customer.Customer;
import controller.Controller;

import java.util.LinkedList;
import java.util.Random;

import model.objects.movingObject.Creature;
import model.objects.movingObject.MovingObject;
import tools.Timer;
import tools.Vector2D;

/**
 * Manipulate all customers in game.
 *
 * @see CustomerSettings
 */
public class CustomersController {
    public final CustomerSettings CUSTOMERS_SETTINGS = new CustomerSettings();

    private final ElevatorSystemController ELEVATOR_SYSTEM_CONTROLLER;
    private final Timer spawnTimer = new Timer();
    private final Controller CONTROLLER;

    public CustomersController(Controller controller) {
        this.ELEVATOR_SYSTEM_CONTROLLER = controller.ELEVATOR_SYSTEM_CONTROLLER;
        this.CONTROLLER = controller;
    }

    public void tick(long deltaTime) {
        spawnTimer.tick(deltaTime);

        createClientIfPossible();
        CONTROLLER.MODEL.CUSTOMERS.forEach(this::processCustomer);

        CONTROLLER.MODEL.CUSTOMERS.forEach(creature -> creature.tick(deltaTime));
    }

    private void processCustomer(Customer customer) {
        switch (customer.getState()) {
            case GO_TO_BUTTON -> processGoToButton(customer);
            case WAIT_UNTIL_ARRIVED -> processWaitUntilArrived(customer);
            case GET_IN -> processGetIn(customer);
        }
    }

    private void createClientIfPossible() {
        if (spawnTimer.isReady() && CONTROLLER.MODEL.CUSTOMERS.size() < CUSTOMERS_SETTINGS.MAX_CUSTOMERS) {
            var maxFloor = ELEVATOR_SYSTEM_CONTROLLER.SETTINGS.FLOORS_COUNT;
            var randomStartFloor = new Random().nextInt(0, maxFloor);
            var randomEndFloor = new Random().nextInt(0, maxFloor);
            randomEndFloor = (maxFloor + randomStartFloor + randomEndFloor - 1) % maxFloor;
            CreateCustomer(randomStartFloor, randomEndFloor);

            spawnTimer.restart(CUSTOMERS_SETTINGS.SPAWN_RATE);
        }
    }

    private void processGoToButton(Customer customer) {
        var buttonPosition = CONTROLLER.MODEL.getBuilding()
                .getClosestButtonOnFloor(customer.getPosition());
        customer.setDestination(buttonPosition);
        if (customer.isReachedDestination()) {
            ELEVATOR_SYSTEM_CONTROLLER.buttonClick(new ElevatorRequest(customer.getPosition(), customer.wantsGoUp()));
            customer.setState(CustomerState.WAIT_UNTIL_ARRIVED);
        }
    }

    private void processWaitUntilArrived(Customer customer) {
        var nearestOpenedElevatorOnFloor = CONTROLLER.MODEL.getBuilding()
                .getClosestOpenedElevatorOnFloor(customer.getPosition(), customer.getCurrentFlor());
        if (nearestOpenedElevatorOnFloor != null) {
            customer.setDestination(nearestOpenedElevatorOnFloor.getPosition());
            customer.setState(CustomerState.GET_IN);
            return;
        }
        if (customer.getMAIN_TIMER().isReady()) {
            var getPositionToWalk = new Vector2D(
                    new Random().nextInt(0, ELEVATOR_SYSTEM_CONTROLLER.SETTINGS.BUILDING_SIZE.x),
                    customer.getPosition().y);
            customer.setDestination(getPositionToWalk);
        }
    }

    private void processGetIn(Customer customer) {
        var closestOpenedElevator = CONTROLLER.MODEL.getBuilding()
                .getClosestOpenedElevatorOnFloor(customer.getPosition(), customer.getCurrentFlor());
        if (closestOpenedElevator == null) {
            customer.setState(CustomerState.GO_TO_BUTTON);
            return;
        }
        customer.setDestination(closestOpenedElevator.getPosition());

        if (customer.isReachedDestination()) {
            ELEVATOR_SYSTEM_CONTROLLER.getCustomerIntoElevator(closestOpenedElevator);
            customer.setCurrentElevator(closestOpenedElevator);

            var makeSpaceInElevator = ELEVATOR_SYSTEM_CONTROLLER.SETTINGS.ELEVATOR_SIZE.x / 2;
            var newDestination = new Vector2D(
                    new Random().nextDouble(
                            -makeSpaceInElevator,
                            makeSpaceInElevator - customer.getSize().x), 0);
            ELEVATOR_SYSTEM_CONTROLLER.setFloorToReach(customer.getCurrentElevator(), customer.getFLOOR_TO_END());
            customer.setDestination(customer.getPosition().add(newDestination));
        }
    }

    public void CreateCustomer(int floorStart, int floorEnd) {
        var startPosition = getStartPositionForCustomer(floorStart);
        var customer = new Customer(floorStart, floorEnd, startPosition, CUSTOMERS_SETTINGS.CUSTOMER_SPEED,
                CUSTOMERS_SETTINGS.CUSTOMER_SIZE);

        customer.setState(CustomerState.GO_TO_BUTTON);
        CONTROLLER.MODEL.CUSTOMERS.add(customer);
    }

    private Vector2D getStartPositionForCustomer(int floorStart) {
        Vector2D startPosition = CONTROLLER.MODEL.getBuilding().getStartPositionAfterBuilding(floorStart);
        // So u can't see customer when he spawns
        if (startPosition.x == 0) {
            startPosition.x -= CUSTOMERS_SETTINGS.CUSTOMER_SIZE.x * 2;
        } else {
            startPosition.x += CUSTOMERS_SETTINGS.CUSTOMER_SIZE.x * 2;
        }
        return startPosition;
    }
}
