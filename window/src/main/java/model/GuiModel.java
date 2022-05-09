package model;


import architecture.tickable.TickableList;
import configs.canvas.ColorConfig;
import configs.canvas.DrawConfig;
import configs.tools.CombienedDrawDataBase;
import configs.ConnectionEstalblishConfig;
import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableCreature;
import model.packageLoader.PackageLoader;
import drawable.concretes.building.floor.Floor;
import drawable.concretes.building.floor.elevatorSpace.ElevatorButton;
import drawable.concretes.customer.DrawableCreatureCustomer;
import drawable.concretes.elevator.DrawableCreatureElevator;
import lombok.Getter;
import model.objects.CreaturesData;
import tools.Pair;
import tools.Vector2D;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class GuiModel implements Model {
    @Getter
    private final CombienedDrawDataBase CombienedDrawDataBase = new CombienedDrawDataBase(new ColorConfig(), new DrawConfig());

    @Getter
    private final List<DrawableCreatureElevator> elevators = new LinkedList<>();
    private final List<DrawableCreatureCustomer> customers = new LinkedList<>();

    private final List<Floor> floors = new LinkedList<>();
    private final List<DrawableCreature> autoDieObjects = new LinkedList<>();

    public void start() {
    }

    public void update() {
        updateFloors(CombienedDrawDataBase.floorsCount());
        updateElevators();
    }

    private void updateElevators() {
        floors.forEach(floor -> floor.updateElevatorBorders(elevators));
    }

    private void updateFloors(int floors_count) {
        int oldFloorsCount = floors.size();
        while (oldFloorsCount < floors_count) {
            floors.add(new Floor(oldFloorsCount++, CombienedDrawDataBase));
        }
        while (oldFloorsCount > floors_count) {
            floors.remove(oldFloorsCount--);
        }
    }

    public LinkedList<Pair<Vector2D, Drawable>> getDrawableOjects() {
        var startGamePosition = new Vector2D(0, 0);
        LinkedList<Pair<Vector2D, Drawable>> drawables = new LinkedList<>();
        autoDieObjects.forEach(drawable -> drawables.addAll(drawable.getDrawables(startGamePosition)));
        customers.forEach(drawable -> drawables.addAll(drawable.getDrawables(startGamePosition)));
        elevators.forEach(drawable -> drawables.addAll(drawable.getDrawables(startGamePosition)));
        floors.forEach(drawable -> drawables.addAll(drawable.getDrawables(startGamePosition)));
        return drawables;
    }

    public TickableList getTickableList() {
        return new TickableList(getDrawableOjects().stream().map(Pair::getSecond).collect(Collectors.toList()));
    }

    public void addMovingDrawable(DrawableCreature text) {
        autoDieObjects.add(text);
    }

    @Override
    public void clearDead() {
        autoDieObjects.removeIf(DrawableCreature::isDead);
    }

    public ElevatorButton getNearestButton(Vector2D data) {
        LinkedList<ElevatorButton> elevatorButtons = new LinkedList<>();
        floors.forEach(floor -> floor.getBorders().forEach(
                border -> elevatorButtons.add(border.getElevatorButton())));

        return elevatorButtons.stream()
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

    public DrawableCreatureElevator getElevator(long id) {
        var ref = new Object() {
            DrawableCreatureElevator foundDrawableElevator;
        };
        elevators.stream().filter(elevator -> elevator.getId() == id).findFirst().ifPresent(
                drawableElevator -> ref.foundDrawableElevator = drawableElevator);
        return ref.foundDrawableElevator;
    }

    public void setRemoteConfig(ConnectionEstalblishConfig connectionEstalblishConfig) {
        CombienedDrawDataBase.setConnectionEstalblishConfig(connectionEstalblishConfig);
    }

    public void changeBehindElevatorForCustomer(long id) {
        customers.stream().filter(drawableCustomer -> drawableCustomer.getId() == id).findFirst().ifPresent(
                DrawableCreatureCustomer::changeBehindElevator);
    }

    public void updateArivedCreaturesData(CreaturesData data) {
        PackageLoader.ApplyCustomers(data.CUSTOMERS, customers, CombienedDrawDataBase);
        PackageLoader.ApplyElevators(data.ELEVATORS, elevators, CombienedDrawDataBase);
    }
}
