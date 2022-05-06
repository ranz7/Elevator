package model.objects.building;

import architecture.tickable.Startable;
import configs.ElevatorSystemSettings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.objects.elevator.Elevator;
import tools.Vector2D;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class Building implements Startable {
    public final ElevatorSystemSettings settings;
    @Getter
    public final List<Elevator> elevators = new LinkedList<>();
    @Getter
    public double wallSize;

    @Override
    public void start() {
        updateElevatorsPosition();
    }

    public void updateElevatorsPosition() {
        this.wallSize = ( settings.BUILDING_SIZE.y) / settings.FLOORS_COUNT;
        double distanceBetweenElevators = ( settings.BUILDING_SIZE.x) / (settings.getElevatorsCount() + 1);
        var elevatorIterator = elevators.iterator();
        for (int i = 0; i < settings.MAX_ELEVATORS_COUNT; i++) {
            var elevator = elevatorIterator.next();

            if (i < settings.getElevatorsCount()) {
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
        return new Vector2D(settings.BUILDING_SIZE.x, (int) (floorStart * wallSize));
    }

    public Vector2D getClosestButtonOnFloor(Vector2D position) {
        if (settings.getElevatorsCount() == 0) {
            return null;
        }
        var nearestButton = new Vector2D(settings.BUILDING_SIZE.x * 2, position.y);
        for (var elevator : elevators) {
            if (!elevator.isVisible()) {
                break;
            }
            var buttonPosition = new Vector2D(elevator.getPosition().x + settings.BUTTON_RELATIVE_POSITION,
                    position.y);
            nearestButton = position.getNearest(buttonPosition, nearestButton);
        }
        return nearestButton;
    }

    public Elevator getClosestOpenedElevatorOnFloor(Vector2D position, int floor) {
        if (!isOpenedElevatorOnFloorExist(floor)) {
            return null;
        }
        if (settings.getElevatorsCount() == 0) {
            return null;
        }
        Elevator closestElevator = null;
        for (var elevator : elevators) {
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
        return elevators.stream().anyMatch(elevator -> elevator.getCurrentFloor() == floor
                && elevator.isOpened()
                && elevator.isFree()
                && elevator.isVisible());
    }


}


