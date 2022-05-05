package model;


import architecture.tickable.Tickable;
import configs.MainInitializationSettings;
import configs.ColorSettings;
import configs.DrawSettings;
import drawable.drawableBase.creatureWithTexture.Drawable;
import drawable.drawableObjectsConcrete.*;
import drawable.drawableObjectsConcrete.building.floor.Floor;
import drawable.drawableObjectsConcrete.building.floor.elevator.ElevatorButton;
import drawable.drawableObjectsConcrete.customer.DrawableCustomer;
import drawable.drawableObjectsConcrete.elevator.DrawableElevator;
import lombok.Getter;
import model.objects.movingObject.Creature;
import model.objects.movingObject.CreaturesData;
import model.objects.movingObject.MovingObject;
import tools.Vector2D;

import java.util.LinkedList;
import java.util.List;


public class GuiModel implements Model {
    // TODO REFACTOR - move to one SUPER SETINGS class
    public final ColorSettings colorSettings = new ColorSettings();
    public final DrawSettings drawSettings = new DrawSettings();
    @Getter
    private MainInitializationSettings mainInitializationSettings;
    //

    //TODO move into DRAW CLIENT OBJECTS
    @Getter
    private final LinkedList<DrawableElevator> elevators = new LinkedList<>();
    private final LinkedList<DrawableCustomer> customers = new LinkedList<>();

    private final LinkedList<Floor> floors = new LinkedList<>();
    private final LinkedList<FlyingText> flyingTexts = new LinkedList<>();


    public Double getWallHeight() {
        return mainInitializationSettings.BUILDING_SIZE.y / mainInitializationSettings.FLOORS_COUNT;
    }

    public Double getDistanceBetweenElevators() {
        return mainInitializationSettings.BUILDING_SIZE.x / (mainInitializationSettings.ELEVATORS_COUNT + 1);
    }

    private void initialiseFirstData() {
        for (int i = 0; i < mainInitializationSettings.FLOORS_COUNT; i++) {
            floors.add(new Floor(i, this));
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

    public LinkedList<Tickable> getTickableList() {
        LinkedList<Tickable> tickables = new LinkedList<>();
        getDrawableOjects().forEach(drawable -> tickables.add((Tickable) drawable));
        return tickables;
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
        this.mainInitializationSettings = mainInitializationSettings;
    }
    public void changeBehindElevatorForCustomer(long id) {
        customers.stream().filter(drawableCustomer -> drawableCustomer.getId() == id).findFirst().ifPresent(
                DrawableCustomer::changeBehindElevator);
    }


    private void applyArrivedData(List<Creature> creatures_came,
                                  LinkedList<? extends Creature> creatures_to_apply) {
        // erease
        creatures_to_apply.removeIf(
                creatureA -> creatures_came.stream().noneMatch(
                        creatureB -> creatureA.getId() == creatureB.getId()));
        // update
        creatures_to_apply.forEach(
                creatureA -> {
                    creatureA.set(creatures_came.stream().filter(
                            creatureB -> creatureA.getId() == creatureB.getId()
                    ).findFirst().get());
                }
        );
    }

    public void updateData(CreaturesData data) {
        this.applyArrivedData(data.ELEVATORS, elevators);
        this.applyArrivedData(data.CUSTOMERS, customers);

        // Add
        data.ELEVATORS.forEach(
                creatureA -> {
                    if (elevators.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())) {
                        elevators.add(
                                new DrawableElevator(creatureA, mainInitializationSettings.ELEVATOR_OPEN_CLOSE_TIME,
                                        colorSettings.elevatorBackGround, colorSettings.elevatorDoor,
                                        colorSettings.elevatorBorder));
                    }
                }
        );

        data.CUSTOMERS.forEach(
                creatureA -> {
                    if (customers.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())) {
                        customers.add(new DrawableCustomer(creatureA,
                                colorSettings.customersSkin));
                    }
                }
        );

    }

}
