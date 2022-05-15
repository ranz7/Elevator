package controller.subControllers;

import connector.Gates;
import controller.ControllerConnector;
import lombok.RequiredArgsConstructor;
import model.objects.elevator.ElevatorRequest;
import model.objects.elevator.Elevator;
import settings.configs.ElevatorSystemConfig;
import connector.protocol.Protocol;
import model.AppModel;
import architecture.tickable.Tickable;

import java.util.stream.Collectors;
import java.util.LinkedList;


/**
 * Manipulate all elevators in game.
 *
 * @see ElevatorSystemConfig
 */
@RequiredArgsConstructor
public class ElevatorsController implements Tickable {
    private final Gates gates;
    private final AppModel appModel;
    private final ControllerConnector connector;
    private final LinkedList<ElevatorRequest> pendingElevatorRequests = new LinkedList<>();

    @Override
    public void tick(double deltaTime) {
        pendingElevatorRequests.removeIf(this::tryToCallElevator);
        for (var elevator : appModel.getBuildings().elevators) {
            elevator.tick(deltaTime);
        }
    }

    public void buttonClick(ElevatorRequest request) {
        gates.send(Protocol.ELEVATOR_BUTTON_CLICK, request.button_position());
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


    private boolean tryToCallElevator(ElevatorRequest request) {
        // closest, free, and go the same way / or wait
        LinkedList<Elevator> elevatorsAvailable = appModel.getBuildings().elevators.stream()
                .filter(Elevator::isAvailable)
                .collect(Collectors.toCollection(LinkedList::new));
        if (elevatorsAvailable.size() == 0) {
            return false;
        }

        Elevator closestElevator = elevatorsAvailable.stream()
                .reduce(null, (elevatorA, elevatorB) -> this.closestElevator(request, elevatorA, elevatorB));

        var requestFloor = (int) Math.round(request.button_position().y / appModel.getBuildings().getWallSize());
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

        var requestFloor = (int) Math.round(request.button_position().y / appModel.getBuildings().getWallSize());
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
            settings.setElevatorsCount(settings.getElevatorsCount() + 1);

        } else {
            settings.setElevatorsCount(settings.getElevatorsCount() - 1);
        }
        //           appModel.getBuilding().updateElevatorsPosition();

    }
}
