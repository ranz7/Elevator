package controller.elevatorSystemController;

import model.objects.elevator.ElevatorRequest;
import model.objects.elevator.ElevatorState;
import model.objects.building.Building;
import model.objects.elevator.Elevator;
import connector.protocol.Protocol;
import controller.Controller;
import model.Model;

import java.util.stream.Collectors;
import java.util.LinkedList;


/**
 * Manipulate all elevators in game.
 *
 * @see ElevatorSystemSettings
 */
public class ElevatorSystemController {
    public final ElevatorSystemSettings SETTINGS = new ElevatorSystemSettings();

    private final LinkedList<ElevatorRequest> PENDING_ELEVATOR_REQUESTS = new LinkedList<>();
    private final Controller CONTROLLER;
    private final Model MODEL;

    public ElevatorSystemController(Controller controller) {
        this.CONTROLLER = controller;
        this.MODEL = CONTROLLER.MODEL;
        MODEL.Initialize(new Building(SETTINGS));
    }

    public void tick(long deltaTime) {
        PENDING_ELEVATOR_REQUESTS.removeIf(this::tryToCallElevator);
        for (var elevator : MODEL.getBuilding().ELEVATORS) {
            if (!elevator.isVisible()) {
                break;
            }
            switch (elevator.getState()) {
                case WAIT -> processWait(elevator);
                case IN_MOTION -> processInMotion(elevator);
                case OPENING, CLOSING -> processOpeningClosing(elevator);
                case OPENED -> processOpened(elevator);
            }
            elevator.tick(deltaTime);
        }
    }

    public void buttonClick(ElevatorRequest request) {
        CONTROLLER.Send(Protocol.ELEVATOR_BUTTON_CLICK, request.button_position());
        if (!tryToCallElevator(request)) {
            PENDING_ELEVATOR_REQUESTS.add(request);
        }
    }

    public void getCustomerIntoElevator(Elevator nearestOpenedElevator) {
        nearestOpenedElevator.put();
    }

    public void getOutFromElevator(Elevator currentElevator) {
        currentElevator.remove();
    }

    public void setFloorToReach(Elevator currentElevator, int floorEnd) {
        currentElevator.addFloorToThrowOut(floorEnd);
        currentElevator.findBestFloor();
    }

    private void processWait(Elevator elevator) {
        if (!elevator.TIMER.isReady()) {
            return;
        }
        int bestFloor = elevator.findBestFloor();
        if (bestFloor != Elevator.UNEXISTING_FLOOR) {
            elevator.setFloorDestination(bestFloor);
            elevator.setState(ElevatorState.IN_MOTION);
        }
    }

    private void processInMotion(Elevator elevator) {
        if (elevator.isReachedDestination()) {
            CONTROLLER.Send(Protocol.ELEVATOR_OPEN, elevator.getId());
            elevator.TIMER.restart(SETTINGS.ELEVATOR_OPEN_CLOSE_TIME);
            elevator.setState(ElevatorState.OPENING);
        }
    }

    private void processOpeningClosing(Elevator elevator) {
        if (!elevator.TIMER.isReady()) {
            return;
        }
        if (elevator.getState() == ElevatorState.OPENING) {
            elevator.TIMER.restart(SETTINGS.ELEVATOR_WAIT_AS_OPENED_TIME);
            elevator.setState(ElevatorState.OPENED);
            elevator.arrived();
        }
        if (elevator.getState() == ElevatorState.CLOSING) {
            elevator.TIMER.restart(SETTINGS.ELEVATOR_AFTER_CLOSE_AFK_TIME);
            elevator.setState(ElevatorState.WAIT);
        }
    }

    private void processOpened(Elevator elevator) {
        if (!elevator.TIMER.isReady()) {
            return;
        }
        elevator.TIMER.restart(SETTINGS.ELEVATOR_OPEN_CLOSE_TIME);
        CONTROLLER.Send(Protocol.ELEVATOR_CLOSE, elevator.getId());
        elevator.setState(ElevatorState.CLOSING);
    }

    private boolean tryToCallElevator(ElevatorRequest request) {
        // closest, free, and go the same way / or wait
        LinkedList<Elevator> elevatorsAvailable = MODEL.getBuilding().ELEVATORS.stream()
                .filter(Elevator::isAvailable)
                .collect(Collectors.toCollection(LinkedList::new));
        if (elevatorsAvailable.size() == 0) {
            return false;
        }

        Elevator closestElevator = elevatorsAvailable.stream()
                .reduce(null, (elevatorA, elevatorB) -> this.closestElevator(request, elevatorA, elevatorB));

        var requestFloor = (int) Math.round(request.button_position().y / MODEL.getBuilding().getWallSize());
        closestElevator.addFloorToPickUp(requestFloor);
        closestElevator.findBestFloor();
        return true;
    }

    private Elevator closestElevator(ElevatorRequest request, Elevator elevatorA, Elevator elevatorB) {
        if (elevatorA == null || !elevatorA.isVisible()) {
            return elevatorB;
        }
        if (elevatorB == null || !elevatorB.isVisible()) {
            return elevatorA;
        }

        var requestFloor = (int) Math.round(request.button_position().y / MODEL.getBuilding().getWallSize());
        double timeToBeForElevatorA = elevatorA.getTimeToBeHere(requestFloor);
        double timeToBeForElevatorB = elevatorB.getTimeToBeHere(requestFloor);
        if (timeToBeForElevatorA > timeToBeForElevatorB) {
            return elevatorB;
        }
        if (timeToBeForElevatorA < timeToBeForElevatorB) {
            return elevatorA;
        }
        if (request.button_position().getNearest(elevatorA.getPosition(), elevatorB.getPosition())
                .equals(elevatorA.getPosition())) {
            return elevatorA;
        }
        return elevatorB;
    }

    public void changeElevatorsCount(boolean data) {
        if (data) {
            if (SETTINGS.getElevatorsCount() < SETTINGS.MAX_ELEVATORS_COUNT) {
                SETTINGS.setElevatorsCount(SETTINGS.getElevatorsCount() + 1);
                MODEL.getBuilding().updateElevatorsPosition();
                return;
            }
        }
        if (SETTINGS.getElevatorsCount() > 0) {
            SETTINGS.setElevatorsCount(SETTINGS.getElevatorsCount() - 1);
            MODEL.getBuilding().updateElevatorsPosition();
        }
    }
}
