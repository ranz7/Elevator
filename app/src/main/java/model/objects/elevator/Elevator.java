package model.objects.elevator;

import controller.subControllers.ElevatorsController;
import model.DatabaseOf;
import model.Transport;
import model.Transportable;
import model.objects.Creature;
import model.objects.CreatureInterface;
import model.objects.floor.FloorStructure;
import model.objects.elevator.Algorithm.BestAlgorithm;
import model.objects.elevator.Algorithm.ElevatorAlgorithm;
import settings.LocalCreaturesSettings;
import lombok.Getter;
import lombok.Setter;
import model.objects.movingObject.MovingCreature;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Timer;
import tools.Vector2D;

/*
 * Elevator is storing all requests under and behind his way, the algorithm finds the closest floor by
 * calculating distance to came from floor A to floor B and all intermediate floors.
 */
public class Elevator extends MovingCreature implements Transportable<Creature> {
    @Setter
    @Getter
    private Transport<Creature> transport;

    @Getter
    @Setter
    private ElevatorState state = ElevatorState.wait;
    public final Timer timer = new Timer();

    private final ElevatorAlgorithm algorithm;
    private final LocalCreaturesSettings settings;
    private final ElevatorsController controller;

    public Elevator(Double x, ElevatorsController controller, LocalCreaturesSettings settings) {
        super(new Vector2D(x, 0), settings.elevatorSize(),
                Trajectory.StayOnPlaceWithConstSpeed(settings.elevatorSpeed()));
        var timeToSpendOfFloor = settings.elevatorOpenCloseTime() * 2 +
                settings.elevatorAfterCloseAfkTime() + settings.elevatorWaitAsOpenedTime();
        algorithm = new BestAlgorithm(timeToSpendOfFloor, settings.maxHumanCapacity(), this::getRawTimeToGetToFloor);
        this.controller = controller;
        this.settings = settings;
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
        timer.tick(deltaTime);
        switch (state) {
            case wait -> processWait();
            case inMotion -> processInMotion();
            case opening, closing -> processOpeningClosing();
            case opened -> processOpened();
        }
    }

    private void processInMotion() {
        if (isReachedDestination()) {
            controller.elevatorDoorsOpen(getId());
            timer.restart(settings.elevatorOpenCloseTime());
            setState(ElevatorState.opening);
        }
    }

    private void processWait() {
        if (!timer.isReady()) {
            return;
        }
        int bestFloor = algorithm.findBestFloor();
        if (bestFloor == -1) {
            return;
        }
        setFloorDestination(bestFloor);
        setState(ElevatorState.inMotion);
    }

    private void processOpeningClosing() {
        if (!timer.isReady()) {
            return;
        }
        if (getState() == ElevatorState.opening) {
            timer.restart(settings.elevatorWaitAsOpenedTime());
            setState(ElevatorState.opened);
            arrived();
        }
        if (getState() == ElevatorState.closing) {
            timer.restart(settings.elevatorAfterCloseAfkTime());
            setState(ElevatorState.wait);
        }
    }

    private void processOpened() {
        if (!timer.isReady()) {
            return;
        }
        timer.restart(settings.elevatorOpenCloseTime());
        controller.elevatorDoorsClose(getId());
        setState(ElevatorState.closing);
    }

    public boolean isAvailable() {
        return algorithm.isAvailable();
    }

    public boolean isFree() {
        return algorithm.isFree();
    }


    public FloorStructure getCurrentFloor() {
        return ((FloorStructure) transport);
    }

    public int getCurrentFloorNum() {
        return ((FloorStructure) transport).getCurrentFloorNum();
    }

    public boolean isOpened() {
        return ElevatorState.opened.equals(state);
    }

    public void addFloorToPickUp(int toAddFloor) {
        algorithm.addFloorToPick(toAddFloor, getCurrentFloorNum());
    }

    public void addCustomer() {
        algorithm.addCustomer();
    }

    public void removeCustomer() {
        algorithm.removeCustomer();
    }

    private void setFloorDestination(int bestFloor) {
        int deltaFloors = bestFloor - getCurrentFloorNum();
        setMoveTrajectory(Trajectory.WithOldSpeedByVector(new Vector2D(0, deltaFloors).multiply(settings.floorSize())));
    }

    public void arrived() {
        algorithm.arrived(getCurrentFloorNum());
    }

    public boolean isInMotion() {
        return state.equals(ElevatorState.inMotion);
    }

    public void addFloorToThrowOut(int floorEnd) {
        algorithm.addFloorToThrowOut(floorEnd, getCurrentFloorNum());
    }

    public double getTimeToBeHere(int requestFloor) {
        return algorithm.getTimeToBeHere(requestFloor, getCurrentFloorNum());
    }

    private Double getRawTimeToGetToFloor(Integer requestFloor) {
        return Math.abs((requestFloor - getCurrentFloorNum()) * settings.floorSize().y - position.y) / getSpeed();
    }
}
