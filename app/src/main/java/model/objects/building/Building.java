package model.objects.building;

import configs.ElevatorSystemSettings;
import lombok.Getter;
import model.objects.elevator.Elevator;
import tools.tools.Vector2D;

import java.util.LinkedList;
import java.util.Random;

public class Building {
    public final LinkedList<Elevator> ELEVATORS = new LinkedList<>();
    public final ElevatorSystemSettings SETTINGS;
    @Getter
    public double wallSize;

    public Building(ElevatorSystemSettings settings) {
        this.SETTINGS = settings;
        for (int i = 0; i < SETTINGS.MAX_ELEVATORS_COUNT; i++) {
            var newElevator = new Elevator(settings);
            newElevator.setVisible(false);
            ELEVATORS.add(newElevator);
        }
        updateElevatorsPosition();
    }

    public void updateElevatorsPosition() {
        this.wallSize = ((double) SETTINGS.BUILDING_SIZE.y) / SETTINGS.FLOORS_COUNT;
        double distanceBetweenElevators = ((double) SETTINGS.BUILDING_SIZE.x) / (SETTINGS.getElevatorsCount() + 1);
        var elevatorIterator = ELEVATORS.iterator();
        for (int i = 0; i < SETTINGS.MAX_ELEVATORS_COUNT; i++) {
            var elevator = elevatorIterator.next();

            if (i < SETTINGS.getElevatorsCount()) {
                elevator.setWallSize(wallSize);
                if (elevator.isVisible()) {
                    elevator.setPosition(new Vector2D(distanceBetweenElevators * (i + 1), elevator.getPosition().y));
                } else {
                    elevator.setVisible(true);
                    elevator.setPosition(new Vector2D(distanceBetweenElevators * (i + 1), 0));
                    elevator.reset();
                }
            } else {
                elevator.setVisible(false);
            }
        }

    }

    public Vector2D getStartPositionAfterBuilding(int floorStart) {
        boolean spawnInLeftCorner = new Random().nextBoolean();
        if (spawnInLeftCorner) {
            return new Vector2D(0, (int) (floorStart * wallSize));
        }
        return new Vector2D(SETTINGS.BUILDING_SIZE.x, (int) (floorStart * wallSize));
    }

    public Vector2D getClosestButtonOnFloor(Vector2D position) {
        if (SETTINGS.getElevatorsCount() == 0) {
            return null;
        }
        var nearestButton = new Vector2D(SETTINGS.BUILDING_SIZE.x * 2, position.y);
        for (var elevator : ELEVATORS) {
            if (!elevator.isVisible()) {
                break;
            }
            var buttonPosition = new Vector2D(elevator.getPosition().x + SETTINGS.BUTTON_RELATIVE_POSITION,
                    position.y);
            nearestButton = position.getNearest(buttonPosition, nearestButton);
        }
        return nearestButton;
    }

    public Elevator getClosestOpenedElevatorOnFloor(Vector2D position, int floor) {
        if (!isOpenedElevatorOnFloorExist(floor)) {
            return null;
        }
        if (SETTINGS.getElevatorsCount() == 0) {
            return null;
        }
        Elevator closestElevator = null;
        for (var elevator : ELEVATORS) {
            if (!elevator.isVisible()) {
                break;
            }
            if (elevator.getCurrentFloor() == floor && elevator.isOpened()) {
                if (closestElevator == null) {
                    closestElevator = elevator;
                    continue;
                }
                var nearestElevatorPosition = position.getNearest(elevator.getPosition(),
                        closestElevator.getPosition());
                if (nearestElevatorPosition == elevator.getPosition()) {
                    closestElevator = elevator;
                }
            }
        }
        return closestElevator;
    }

    private boolean isOpenedElevatorOnFloorExist(int floor) {
        return ELEVATORS.stream().anyMatch(elevator -> elevator.getCurrentFloor() == floor
                && elevator.isOpened()
                && elevator.isFree()
                && elevator.isVisible());
    }
}


