package model;


import architecture.tickable.TickableList;
import databases.configs.canvas.ColorConfig;
import databases.configs.canvas.DrawConfig;
import databases.CombienedDrawDataBase;
import configs.ConnectionEstalblishConfig;
import drawable.abstracts.DrawableCreature;
import drawable.abstracts.DrawableMovingCreature;
import drawable.concretes.building.floor.decorations.FloorPainting;
import drawable.concretes.building.floor.elevatorSpace.ElevatorBorder;
import model.packageLoader.PackageLoader;
import drawable.concretes.building.floor.Floor;
import drawable.concretes.building.floor.elevatorSpace.ElevatorButton;
import drawable.concretes.customer.DrawableCustomerCreature;
import drawable.concretes.elevator.DrawableElevatorCreature;
import lombok.Getter;
import model.objects.CreaturesData;
import tools.Pair;
import tools.Vector2D;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class GuiModel implements Model {
    @Getter
    private final CombienedDrawDataBase combienedDrawDataBase = new CombienedDrawDataBase(new ColorConfig(), new DrawConfig());

    @Getter
    private final List<DrawableElevatorCreature> elevators = new LinkedList<>();
    private final List<DrawableCustomerCreature> customers = new LinkedList<>();

    private final List<Floor> floors = new LinkedList<>();
    private final List<DrawableMovingCreature> autoDieObjects = new LinkedList<>();

    public void start() {
    }

    public void update() {
        int oldFloorsCount = floors.size();
        var random = new Random(combienedDrawDataBase.picturesGeneratorSeed());
        while (oldFloorsCount < combienedDrawDataBase.floorsCount()) {
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

    public LinkedList<Pair<Vector2D, DrawableCreature>> getDrawableOjects() {
        var startGamePosition = new Vector2D(0, 0);
        LinkedList<Pair<Vector2D, DrawableCreature>> drawablesAndPositions = new LinkedList<>();
        autoDieObjects.forEach(drawable -> drawablesAndPositions.addAll(drawable.getDrawablesAndAbsoluteDrawPositions(startGamePosition)));
        customers.forEach(drawable -> drawablesAndPositions.addAll(drawable.getDrawablesAndAbsoluteDrawPositions(startGamePosition)));
        elevators.forEach(drawable -> drawablesAndPositions.addAll(drawable.getDrawablesAndAbsoluteDrawPositions(startGamePosition)));
        floors.forEach(drawable -> drawablesAndPositions.addAll(drawable.getDrawablesAndAbsoluteDrawPositions(startGamePosition)));
        return drawablesAndPositions;
    }

    public TickableList getTickableList() {
        return new TickableList(getDrawableOjects().stream().map(Pair::getSecond).collect(Collectors.toList()));
    }

    public void addMovingDrawable(DrawableMovingCreature text) {
        autoDieObjects.add(text);
    }

    @Override
    public void clearDead() {
        autoDieObjects.removeIf(DrawableMovingCreature::isDead);
    }

    public ElevatorButton getNearestButton(Vector2D data) {
        return streamOf(ElevatorButton.class)
                .reduce(null, (elevatorButtonA, elevatorButtonB) -> {
                    if (elevatorButtonA == null) {
                        return elevatorButtonB;
                    }
                    if (elevatorButtonB == null) {
                        return elevatorButtonA;
                    }
                    if (data.getNearest(elevatorButtonA.getPosition(), elevatorButtonB.getPosition())
                            .equals(elevatorButtonA.getPosition())) {
                        return elevatorButtonA;
                    }
                    return elevatorButtonB;
                });
    }

    private <T extends DrawableCreature> Stream<T> streamOf(Class<T> classToFind) {
        return (Stream<T>) getDrawableOjects().stream()
                .filter(object -> object.getClass().equals(classToFind)).map(Pair::getSecond);
    }

    public DrawableCustomerCreature getCustomer(long id) {
        var ref = new Object() {
            DrawableCustomerCreature customer;
        };
        customers.stream().filter(elevator -> elevator.getId() == id).findFirst().ifPresent(
                customer -> ref.customer = customer);
        return ref.customer;
    }

    public DrawableElevatorCreature getElevator(long id) {
        var ref = new Object() {
            DrawableElevatorCreature foundDrawableElevator;
        };
        elevators.stream().filter(elevator -> elevator.getId() == id).findFirst().ifPresent(
                drawableElevator -> ref.foundDrawableElevator = drawableElevator);
        return ref.foundDrawableElevator;
    }

    public void setRemoteConfig(ConnectionEstalblishConfig connectionEstalblishConfig) {
        combienedDrawDataBase.setConnectionEstalblishConfig(connectionEstalblishConfig);
    }

    public void updateArivedCreaturesData(CreaturesData data) {
        PackageLoader.ApplyCustomers(data.CUSTOMERS, customers, combienedDrawDataBase);
        PackageLoader.ApplyElevators(data.ELEVATORS, elevators, combienedDrawDataBase);
    }

}
