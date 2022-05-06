package model;


import architecture.tickable.TickableList;
import configs.CanvasSettings.ColorSettings;
import configs.CanvasSettings.DrawSettings;
import configs.CanvasSettings.MainSettings;
import configs.MainInitializationSettings;
import drawable.drawableBase.Drawable;
import model.packageLoader.PackageLoader;
import drawable.drawableObjectsConcrete.*;
import drawable.drawableObjectsConcrete.building.floor.Floor;
import drawable.drawableObjectsConcrete.building.floor.elevator.ElevatorButton;
import drawable.drawableObjectsConcrete.customer.DrawableCustomer;
import drawable.drawableObjectsConcrete.elevator.DrawableElevator;
import lombok.Getter;
import model.objects.CreaturesData;
import model.objects.movingObject.MovingObject;
import tools.Vector2D;

import java.util.LinkedList;
import java.util.List;


public class GuiModel implements Model {
    @Getter
    private final MainSettings mainSettings = new MainSettings(new ColorSettings(), new DrawSettings());

    //TODO move into DRAW CLIENT OBJECTS
    @Getter
    private final List<DrawableElevator> elevators = new LinkedList<>();
    private final List<DrawableCustomer> customers = new LinkedList<>();

    private final List<Floor> floors = new LinkedList<>();
    private final List<FlyingText> flyingTexts = new LinkedList<>();

    public void start() {
    }

    public void update() {
        updateFloors(mainSettings.floorsCount());
    }

    private void updateFloors(int floors_count) {
        int oldFloorsCount = floors.size();
        while (oldFloorsCount < floors_count) {
            floors.add(new Floor(oldFloorsCount++, getElevators(), mainSettings));
        }
        while (oldFloorsCount > floors_count) {
            floors.remove(oldFloorsCount--);
        }
    }

    public LinkedList<Drawable> getDrawableOjects() {
        LinkedList<Drawable> drawables = new LinkedList<>();
        elevators.forEach(drawable -> drawables.addAll(drawable.getDrawables()));
        floors.forEach(drawable -> drawables.addAll(drawable.getDrawables()));
        drawables.addAll(customers.stream().toList());
        drawables.addAll(flyingTexts);
        return drawables;
    }

    public TickableList getTickableList() {
        return new TickableList(getDrawableOjects());
    }

    public void addMovingDrawable(FlyingText text) {
        flyingTexts.add(text);
    }

    @Override
    public void clearDead() {
        flyingTexts.removeIf(MovingObject::isDead);
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

    public void setMainInitializationSettings(MainInitializationSettings mainInitializationSettings) {
        mainSettings.set(mainInitializationSettings);
    }

    public void changeBehindElevatorForCustomer(long id) {
        customers.stream().filter(drawableCustomer -> drawableCustomer.getId() == id).findFirst().ifPresent(
                DrawableCustomer::changeBehindElevator);
    }

    public void updateData(CreaturesData data) {
        PackageLoader.ApplyCustomers(data.CUSTOMERS, customers, mainSettings);
        PackageLoader.ApplyElevators(data.ELEVATORS, elevators, mainSettings);
    }
}
