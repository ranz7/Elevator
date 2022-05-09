package model.objects.building;

import configs.ElevatorSystemConfig;
import lombok.Getter;
import model.objects.elevator.Elevator;
import tools.Vector2D;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Building {
    public final ElevatorSystemConfig settings;
    @Getter
    public final List<Elevator> elevators = new LinkedList<>();
    @Getter
    public double wallSize;

    public Building(ElevatorSystemConfig settings) {
        this.settings = settings;
        double distanceBetweenElevators = (settings.buildingSize.x) / (settings.getElevatorsCount() + 1);

        this.wallSize = (settings.buildingSize.y) / settings.floorsCount;

        for (int i = 0; i < settings.maxElevatorsCount; i++) {
            var newElevator = new Elevator(new Vector2D(distanceBetweenElevators * (i + 1), 0),settings);
            newElevator.setWallSize(wallSize);
            newElevator.setVisible(false);
            elevators.add(newElevator);
        }

    }

    public Vector2D getStartPositionAfterBuilding(int floorStart) {
        boolean spawnInLeftCorner = new Random().nextBoolean();
        if (spawnInLeftCorner) {
            return new Vector2D(0, (int) (floorStart * wallSize));
        }
        return new Vector2D(settings.buildingSize.x, (int) (floorStart * wallSize));
    }

    public Vector2D getClosestButtonOnFloor(Vector2D position) {
        if (settings.getElevatorsCount() == 0) {
            return null;
        }
        var nearestButton = new Vector2D(settings.buildingSize.x * 2, position.y);
        for (var elevator : elevators) {
            if (!elevator.isVisible()) {
                break;
            }
            var buttonPosition = new Vector2D(elevator.getPosition().x + settings.buttonRelativePosition,
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


