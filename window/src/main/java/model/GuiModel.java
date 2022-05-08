package model;


import architecture.tickable.TickableList;
import configs.canvas.ColorConfig;
import configs.canvas.DrawConfig;
import configs.tools.CombienedDrawDataBase;
import configs.ConnectionEstalblishConfig;
import drawable.drawableAbstract.Drawable;
import drawable.drawableAbstract.DrawableLocalMoving;
import drawable.drawableConcrete.text.DrawableLocalText;
import model.packageLoader.PackageLoader;
import drawable.drawableConcrete.building.floor.Floor;
import drawable.drawableConcrete.building.floor.elevator.ElevatorButton;
import drawable.drawableConcrete.customer.DrawableCustomer;
import drawable.drawableConcrete.elevator.DrawableElevator;
import lombok.Getter;
import model.objects.CreaturesData;
import tools.Vector2D;

import java.util.LinkedList;
import java.util.List;


public class GuiModel implements Model {
    @Getter
    private final CombienedDrawDataBase CombienedDrawDataBase = new CombienedDrawDataBase(new ColorConfig(), new DrawConfig());

    @Getter
    private final List<DrawableElevator> elevators = new LinkedList<>();
    private final List<DrawableCustomer> customers = new LinkedList<>();

    private final List<Floor> floors = new LinkedList<>();
    private final List<DrawableLocalText> drawableTexts = new LinkedList<>();

    public void start() {
    }

    public void update() {
        updateFloors(CombienedDrawDataBase.floorsCount());
        updateElevators();
    }

    private void updateElevators() {
        floors.forEach(floor->floor.updateElevatorBorders(elevators));
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

    public LinkedList<Drawable> getDrawableOjects() {
        LinkedList<Drawable> drawables = new LinkedList<>();
 //       elevators.forEach(drawable -> drawables.addAll(drawable.getDrawables()));
        floors.forEach(drawable -> drawables.addAll(drawable.getDrawables()));
//        drawables.addAll(customers.stream().toList());
//        drawables.addAll(drawableTexts);
        return drawables;
    }

    public TickableList getTickableList() {
        return new TickableList(getDrawableOjects());
    }

    public void addMovingDrawable(DrawableLocalText text) {
        drawableTexts.add(text);
    }

    @Override
    public void clearDead() {
        drawableTexts.removeIf(DrawableLocalMoving::isDead);
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

    public DrawableElevator getElevator(long id) {
        var ref = new Object() {
            DrawableElevator foundDrawableElevator;
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
                DrawableCustomer::changeBehindElevator);
    }

    public void updateData(CreaturesData data) {
        PackageLoader.ApplyCustomers(data.CUSTOMERS, customers, CombienedDrawDataBase);
        PackageLoader.ApplyElevators(data.ELEVATORS, elevators, CombienedDrawDataBase);
    }
}
