package model.objects.customer.StandartCustomer;

import controller.subControllers.CustomersController;
import lombok.Getter;
import model.Transport;
import model.Transportable;
import model.objects.floor.FloorStructure;
import model.objects.elevator.Elevator;
import model.objects.movingObject.MovingCreature;
import settings.LocalCreaturesSettings;
import lombok.Setter;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Timer;
import tools.Vector2D;

import java.util.Random;

import static model.objects.customer.StandartCustomer.StandartCustomerState.goToButton;


public class StandartCustomer extends MovingCreature implements Transportable {
    @Setter
    @Getter
    private Transport transport;
    private final FloorStructure floorToGetOut;
    private final LocalCreaturesSettings settings;
    private final Timer mainTimer = new Timer();
    private final CustomersController controller;

    @Setter
    private StandartCustomerState state = goToButton;

    public StandartCustomer(FloorStructure floorToGetOut, Double position, CustomersController controller, LocalCreaturesSettings settings) {
        super(new Vector2D(position, 0),
                settings.customerSize(),
                Trajectory.StayOnPlaceWithConstSpeed(settings.customerRandomSpeed()));
        this.floorToGetOut = floorToGetOut;
        this.settings = settings;
        this.controller = controller;
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
        switch (state) {
            case goToButton -> processGoToButton();
            case waitUntilArrived -> processWaitUntilArrived();
            case getIn -> processGetIn();
            case stayIn -> processStayIn();
            case getOut -> processGetOut();
        }

        mainTimer.tick(deltaTime);
    }


    public boolean wantsGoUp() {
        assertTransport(FloorStructure.class);
        return ((FloorStructure) transport).getCurrentFloorNum() < floorToGetOut.getCurrentFloorNum();
    }

    private void processGoToButton() {
        assertTransport(FloorStructure.class);
        var button = ((FloorStructure) transport).getClosestButtonOnFloor(getPosition());
        if(button==null){
            return;
        }
        setMoveTrajectory(Trajectory.WithOldSpeedToTheDestination(() -> button.getPosition().onlyY(0)));
        if (isReachedDestination()) {
            button.click(wantsGoUp());
            mainTimer.restart(settings.customerWaitAfterClick());
            setState(StandartCustomerState.waitUntilArrived);
        }
    }

    private void processWaitUntilArrived() {
        var nearestOpenedElevatorOnFloor =
                ((FloorStructure) transport).getClosestOpenedElevatorOnFloor(getPosition());
        if (nearestOpenedElevatorOnFloor != null) {
            setMoveTrajectory(Trajectory.WithOldSpeedToTheDestination(
                    nearestOpenedElevatorOnFloor::getPosition));
            setState(StandartCustomerState.getIn);
            return;
        }
        if (mainTimer.isReady()) {
            var getPositionToWalk = new Vector2D(new Random().nextInt(0, (int) settings.floorSize().x), 0);
            setMoveTrajectory(Trajectory.WithOldSpeedToTheDestination(() -> getPositionToWalk));
            setSpeedCoefficient(settings.slowSpeedCustomerMultiply());
            mainTimer.restart(settings.customerTimeToWalk());

        }
    }


    private void processGetIn() {
        var closestOpenedElevator = ((FloorStructure) transport).getClosestOpenedElevatorOnFloor(getPosition());
        if (closestOpenedElevator == null) {
            setState(goToButton);
            setMoveTrajectory(new Trajectory().WithNewConstSpeedToOldDestination(getSpeed()));
            return;
        }

        if (isReachedDestination()) {
            controller.customerGetIntoElevator(this, closestOpenedElevator);

            var makeSpaceInElevator = closestOpenedElevator.getSize().x / 2;
            var newDestination = new Vector2D(
                    new Random().nextDouble(
                            -makeSpaceInElevator,
                            makeSpaceInElevator - getSize().x), 0);

            closestOpenedElevator.addFloorToThrowOut(floorToGetOut.getCurrentFloorNum());
            var shiftToMakeSpaceInElevator = getPosition().add(newDestination);
            setMoveTrajectory(Trajectory.ToTheDestination(
                    getSpeed(),
                    () -> shiftToMakeSpaceInElevator));
            setState(StandartCustomerState.stayIn);
        }
    }

    private void processStayIn() {
        Elevator elevator = (Elevator) transport;
        if (elevator.isOpened()) {
            if (elevator.getCurrentFloorNum() == floorToGetOut.getCurrentFloorNum()) {
                setSpeedCoefficient(1.);
                setMoveTrajectory(Trajectory.WithOldSpeedToTheDestination(elevator::getPosition));
                controller.customerGetOutFromElevator(this, elevator);
                setState(StandartCustomerState.getOut);
            }
        } else {
            setSpeedCoefficient(999.);
        }
    }

    private void processGetOut() {
        if (!isReachedDestination()) {
            return;
        }
        if (getPosition().x > ((FloorStructure) transport).getSize().x + getSize().x || getPosition().x < -getSize().x) {
            setDead(true);
            return;
        }
        setMoveTrajectory(
                Trajectory.WithOldSpeedToTheDestination(
                        () -> new Vector2D(((FloorStructure) transport).getStartPositionAfterBuilding(), 0)
                ));
    }

    private void assertTransport(Class<?> classToCheck) {
        if (!(transport.getClass().isAssignableFrom(classToCheck))) {
            throw new RuntimeException("The transport is badly set.");
        }
    }

}