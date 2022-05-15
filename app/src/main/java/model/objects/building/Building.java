package model.objects.building;

import architecture.tickable.Tickable;
import model.DataBaseOfCreatures;
import settings.LocalObjectsSettings;
import lombok.Getter;
import model.objects.Creature;
import model.objects.customer.Customer;
import model.objects.elevator.Elevator;
import tools.Vector2D;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Building extends Creature {
    private final DataBaseOfCreatures dataBase = new DataBaseOfCreatures(this);
    public final LocalObjectsSettings settings;

    public Building(Vector2D position, DataBaseOfCreatures dataBaseOfCreatures, LocalObjectsSettings settings) {
        super(position, settings.buildingSize());
        this.settings = settings;
        dataBaseOfCreatures.register(dataBaseOfCreatures);
        settings.distanceBetweenElevators();

        for (int i = 0; i < settings.elevatorsCount(); i++) {
            dataBase.add(
                    new Elevator(new Vector2D(settings.distanceBetweenElevators() * (i + 1),
                            0), settings));
        }

        var random = new Random(settings.picturesGeneratorSeed());
        for(int i = 0; i < settings.floorsCount(); i++){
            var floor = new Floor(oldFloorsCount++, combienedDrawDataBase);
            for (var elevator : elevators) {
                floor.addSubDrawable(
                        new ElevatorBorder(new Vector2D(elevator.getPosition().x, 0), elevator, combienedDrawDataBase));
                var decorationPosition = new Vector2D(elevator.getPosition().x + combienedDrawDataBase.distanceBetweenElevators() / 2,
                        combienedDrawDataBase.floorHeight() / 2);
                floor.addSubDrawable(
                        new FloorPainting(decorationPosition, combienedDrawDataBase, random));
            }
            var decorationPosition = new Vector2D(combienedDrawDataBase.distanceBetweenElevators() / 2,
                    combienedDrawDataBase.floorHeight() / 2);
            floor.addSubDrawable(
                    new FloorPainting(decorationPosition, combienedDrawDataBase, random));
            floors.add(floor);
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


    public LinkedList<Customer> getCreaturesList() {
        return customers;
    }
}


