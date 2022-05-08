package controller;

import configs.CustomerConfig;
import lombok.Getter;
import architecture.tickable.Tickable;
import model.objects.movingObject.trajectory.Trajectory;
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
 * @see CustomerConfig
 */
public class CustomersConductor implements Tickable {
    @Getter
    private final CustomerConfig settings = new CustomerConfig();
    private final ElevatorsConductor ELEVATOR_SYSTEM_CONTROLLER;
    private final Timer spawnTimer = new Timer();
    private final AppController AppCONTROLLER;
    AppModel appModel;

    public CustomersConductor(AppController appController) {
        this.ELEVATOR_SYSTEM_CONTROLLER = appController.elevatorsConductor;
        this.AppCONTROLLER = appController;
    }

    @Override
    public void tick(double deltaTime) {
        spawnTimer.tick(deltaTime);

        if (spawnTimer.isReady() && AppCONTROLLER.appModel.customers.size() < settings.maxCustomers) {
            var maxFloor = ELEVATOR_SYSTEM_CONTROLLER.getSettings().floorsCount;
            var randomStartFloor = new Random().nextInt(0, maxFloor);
            var randomEndFloor = new Random().nextInt(0, maxFloor);
            randomEndFloor = (maxFloor + randomStartFloor + randomEndFloor - 1) % maxFloor;
            CreateCustomer(randomStartFloor, randomEndFloor);

            spawnTimer.restart(settings.spawnRate);
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
        customer.setMoveTrajectory(Trajectory.WithOldSpeedToTheDestination(buttonPosition));
        if (customer.isReachedDestination()) {
            ELEVATOR_SYSTEM_CONTROLLER.buttonClick(new ElevatorRequest(customer.getPosition(), customer.wantsGoUp()));
            customer.MAIN_TIMER.restart(settings.timeToWaitAfterButtonClick);
            customer.setState(CustomerState.WAIT_UNTIL_ARRIVED);
        }
    }

    private void processWaitUntilArrived(Customer customer) {
        var nearestOpenedElevatorOnFloor = AppCONTROLLER.appModel.getBuilding()
                .getClosestOpenedElevatorOnFloor(customer.getPosition(), customer.getCurrentFlor());
        if (nearestOpenedElevatorOnFloor != null) {
            customer.setMoveTrajectory(Trajectory.ToTheDestination(
                    customer.getConstSpeed(), // settings.FAST_SPEED_MULTIPLY
                    nearestOpenedElevatorOnFloor.getPosition()));
            customer.setState(CustomerState.GET_IN);
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
        var closestOpenedElevator = AppCONTROLLER.appModel.getBuilding()
                .getClosestOpenedElevatorOnFloor(customer.getPosition(), customer.getCurrentFlor());
        if (closestOpenedElevator == null) {
            customer.setState(CustomerState.GO_TO_BUTTON);
            customer.setMoveTrajectory(Trajectory.ToTheDestination(
                    customer.getConstSpeed(), // * 1
                    closestOpenedElevator.getPosition()));
            return;
        }

        if (customer.isReachedDestination()) {
            ELEVATOR_SYSTEM_CONTROLLER.getCustomerIntoElevator(closestOpenedElevator);
            AppCONTROLLER.Send(Protocol.CUSTOMER_GET_IN_OUT, customer.getId());
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
            customer.setState(CustomerState.STAY_IN);
        }
    }

    private void processStayIn(Customer customer) {
        if (customer.getCurrentElevator().isOpened()) {
            if (customer.getCurrentFlor() == customer.getFLOOR_TO_END()) {
                ELEVATOR_SYSTEM_CONTROLLER.getOutFromElevator(customer.getCurrentElevator());
                customer.setMoveTrajectory(
                        Trajectory.WithOldSpeedToTheDestination(
                                customer.getCurrentElevator().getPosition()));
                customer.setState(CustomerState.GET_OUT);
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
        AppCONTROLLER.Send(Protocol.CUSTOMER_GET_IN_OUT, customer.getId());
    }

    public void CreateCustomer(int floorStart, int floorEnd) {
        double speed = new Random().nextDouble(
                settings.customerSpeed
                        - settings.customerSpeed / 3,
                settings.customerSpeed
                        + settings.customerSpeed / 3);
        var startPosition = getStartPositionForCustomer(floorStart);
        var customer = new Customer(floorStart, floorEnd, startPosition, speed,
                settings.customerSize);

        customer.setState(CustomerState.GO_TO_BUTTON);
        AppCONTROLLER.appModel.customers.add(customer);
    }

    private Vector2D getStartPositionForCustomer(int floorStart) {
        Vector2D startPosition = AppCONTROLLER.appModel.getBuilding().getStartPositionAfterBuilding(floorStart);
        // So u can't see customer when he spawns
        if (startPosition.x == 0) {
            startPosition.x -= settings.customerSize.x * 2;
        } else {
            startPosition.x += settings.customerSize.x * 2;
        }
        return startPosition;
    }

    public void setModel(AppModel appModel) {
        this.appModel = appModel;
    }
}
