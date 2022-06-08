package controller.subControllers;

import lombok.RequiredArgsConstructor;
import model.objects.GameMap;
import model.objects.floor.ElevatorButton;
import model.objects.floor.FloorStructure;
import model.objects.elevator.ElevatorRequest;
import model.objects.elevator.Elevator;
import protocol.Protocol;
import controller.Tickable;
import settings.LocalCreaturesSettings;
import tools.Vector2D;

import java.util.stream.Collectors;
import java.util.LinkedList;


/**
 * Manipulate all elevators in game.
 *
 * @see ElevatorSystemConfig
 */
@RequiredArgsConstructor
public class ElevatorsController implements Tickable {
    private final GameMap gameMap;
    private final FloorStructure baseFloorStructure;
    private final LocalCreaturesSettings settings;
    private final LinkedList<ElevatorRequest> pendingElevatorRequests = new LinkedList<>();

    @Override
    public void tick(double deltaTime) {
        pendingElevatorRequests.removeIf(this::tryToCallElevator);
    }

    public void buttonClick(Integer floorNum, ElevatorButton button, boolean goUp) {
        gameMap.send(Protocol.ELEVATOR_BUTTON_CLICK, button.getId());
        var request = new ElevatorRequest(floorNum, button.getPosition(), goUp);
        pendingElevatorRequests.add(request);
    }


    public void addCustomer(Elevator nearestOpenedElevator) {
        nearestOpenedElevator.addCustomer();
    }

    public void removeCustomer(Elevator currentElevator) {
        currentElevator.removeCustomer();
    }

    private boolean tryToCallElevator(ElevatorRequest request) {
        // closest, free, and go the same way / or wait
        LinkedList<Elevator> elevatorsAvailable = gameMap.getLocalDataBase().streamOf(Elevator.class)
                .filter(Elevator::isAvailable)
                .collect(Collectors.toCollection(LinkedList::new));
        if (elevatorsAvailable.size() == 0) {
            return false;
        }

        Elevator closestElevator = elevatorsAvailable.stream()
                .reduce(null, (elevatorA, elevatorB) -> this.closestElevator(request, elevatorA, elevatorB));

        closestElevator.addFloorToPickUp(request.floorNum());
        return true;
    }

    private Elevator closestElevator(ElevatorRequest request, Elevator elevatorA, Elevator elevatorB) {
        if (elevatorA == null || !elevatorA.isVisible()) {
            return elevatorB;
        }
        if (elevatorB == null || !elevatorB.isVisible()) {
            return elevatorA;
        }

        var requestFloor = request.floorNum();
        double timeToBeForElevatorA = elevatorA.getTimeToBeHere(requestFloor);
        double timeToBeForElevatorB = elevatorB.getTimeToBeHere(requestFloor);
        if (timeToBeForElevatorA > timeToBeForElevatorB) {
            return elevatorB;
        }
        if (timeToBeForElevatorA < timeToBeForElevatorB) {
            return elevatorA;
        }
        var absoluteButtonPosition = request.buttonPosition().addByY(
                requestFloor * settings.floorSize().y);
        var absoluteElevatorPositionA = elevatorA.getPosition().addByY(
                elevatorA.getCurrentFloorNum() * settings.floorSize().y);
        var absoluteElevatorPositionB = elevatorB.getPosition().addByY(
                elevatorB.getCurrentFloorNum() * settings.floorSize().y);
        if (absoluteButtonPosition.getNearest(
                        absoluteElevatorPositionA,
                        absoluteElevatorPositionB)
                .equals(elevatorA.getPosition())) {
            return elevatorA;
        }
        return elevatorB;
    }

    public void changeElevatorsCount(boolean data) {
        if (data) {
//            baseFloorStructure.addButton(42);
        } else {
//            baseFloorStructure.removeElevator()
        }
        //           appModel.getBuilding().updateElevatorsPosition();
    }

    public void elevatorDoorsOpen(int elevatoId) {
        gameMap.send(Protocol.ELEVATOR_OPEN, elevatoId);
    }

    public void elevatorDoorsClose(int elevatorId) {
        gameMap.send(Protocol.ELEVATOR_CLOSE, elevatorId);
    }
}
