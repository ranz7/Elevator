package model.objects.building;

<<<<<<< Updated upstream
import configs.ElevatorSystemSettings;
=======
import architecture.tickable.Tickable;
import model.DataBaseOfCreatures;
import settings.LocalObjectsSettings;
>>>>>>> Stashed changes
import lombok.Getter;
import model.objects.Creature;
import model.objects.customer.Customer;
import model.objects.elevator.Elevator;
import tools.tools.Vector2D;

import java.util.LinkedList;
import java.util.Random;

<<<<<<< Updated upstream
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
=======
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
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======


    public LinkedList<Customer> getCreaturesList() {
        return customers;
    }
>>>>>>> Stashed changes
}


