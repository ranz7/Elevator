package controller;

import model.objects.elevator.ElevatorRequest;
import model.objects.elevator.ElevatorState;
import model.objects.building.Building;
import model.objects.elevator.Elevator;
import configs.ElevatorSystemSettings;
import connector.protocol.Protocol;
import model.AppModel;
import lombok.Getter;

import java.util.stream.Collectors;
import java.util.LinkedList;


/**
 * Manipulate all elevators in game.
 * @see ElevatorSystemSettings
 */
public class ElevatorsConductor {
    @Getter
    private final ElevatorSystemSettings settings = new ElevatorSystemSettings();
    private final LinkedList<ElevatorRequest> pendingElevatorRequests = new LinkedList<>();
    private final AppController appController;

    private AppModel appModel;

    public ElevatorsConductor(AppController appController) {
        this.appController = appController;
    }
    public void setModel(AppModel appModel){
        this.appModel = appModel;
        appModel.Initialize(new Building(settings));
    }
    public void tick(long deltaTime) {
        pendingElevatorRequests.removeIf(this::tryToCallElevator);
        for (var elevator : appModel.getBuilding().ELEVATORS) {
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
        appController.Send(Protocol.ELEVATOR_BUTTON_CLICK, request.button_position());
        if (!tryToCallElevator(request)) {
            pendingElevatorRequests.add(request);
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
            appController.Send(Protocol.ELEVATOR_OPEN, elevator.getId());
            elevator.TIMER.restart(settings.ELEVATOR_OPEN_CLOSE_TIME);
            elevator.setState(ElevatorState.OPENING);
        }
    }

    private void processOpeningClosing(Elevator elevator) {
        if (!elevator.TIMER.isReady()) {
            return;
        }
        if (elevator.getState() == ElevatorState.OPENING) {
            elevator.TIMER.restart(settings.ELEVATOR_WAIT_AS_OPENED_TIME);
            elevator.setState(ElevatorState.OPENED);
            elevator.arrived();
        }
        if (elevator.getState() == ElevatorState.CLOSING) {
            elevator.TIMER.restart(settings.ELEVATOR_AFTER_CLOSE_AFK_TIME);
            elevator.setState(ElevatorState.WAIT);
        }
    }

    private void processOpened(Elevator elevator) {
        if (!elevator.TIMER.isReady()) {
            return;
        }
        elevator.TIMER.restart(settings.ELEVATOR_OPEN_CLOSE_TIME);
        appController.Send(Protocol.ELEVATOR_CLOSE, elevator.getId());
        elevator.setState(ElevatorState.CLOSING);
    }

    private boolean tryToCallElevator(ElevatorRequest request) {
        // closest, free, and go the same way / or wait
        LinkedList<Elevator> elevatorsAvailable = appModel.getBuilding().ELEVATORS.stream()
                .filter(Elevator::isAvailable)
                .collect(Collectors.toCollection(LinkedList::new));
        if (elevatorsAvailable.size() == 0) {
            return false;
        }

        Elevator closestElevator = elevatorsAvailable.stream()
                .reduce(null, (elevatorA, elevatorB) -> this.closestElevator(request, elevatorA, elevatorB));

        var requestFloor = (int) Math.round(request.button_position().y / appModel.getBuilding().getWallSize());
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

        var requestFloor = (int) Math.round(request.button_position().y / appModel.getBuilding().getWallSize());
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
            if (settings.getElevatorsCount() < settings.MAX_ELEVATORS_COUNT) {
                settings.setElevatorsCount(settings.getElevatorsCount() + 1);
                appModel.getBuilding().updateElevatorsPosition();
                return;
            }
        }
        if (settings.getElevatorsCount() > 0) {
            settings.setElevatorsCount(settings.getElevatorsCount() - 1);
            appModel.getBuilding().updateElevatorsPosition();
        }
    }
}
