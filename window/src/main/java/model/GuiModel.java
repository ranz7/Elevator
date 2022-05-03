package model;


import connector.protocol.SettingsData;
import controller.elevatorSystemController.ElevatorSystemSettings;
import drawable.ColorSettings;
import drawable.DrawSettings;
import drawable.drawableBase.creatureWithTexture.Drawable;
import drawable.drawableObjectsConcrete.*;
import drawable.drawableObjectsConcrete.building.floor.Floor;
import drawable.drawableObjectsConcrete.building.floor.elevator.ElevatorButton;
import drawable.drawableObjectsConcrete.customer.DrawableCustomer;
import drawable.drawableObjectsConcrete.elevator.DrawableElevator;
import lombok.Getter;
import model.objects.customer.Customer;
import model.objects.elevator.Elevator;
import model.objects.movingObject.MovingObject;
import tools.Vector2D;

import java.awt.*;
import java.util.LinkedList;


public class GuiModel {
    // TODO REFACTOR - move to one SUPER SETINGS class
    public final ColorSettings COLOR_SETTINGS = new ColorSettings();
    public final DrawSettings DRAW_SETTINGS = new DrawSettings();
    @Getter
    private final SettingsData settings = new SettingsData();
    //

    //TODO move into DRAW CLIENT OBJECTS
    @Getter
    private final LinkedList<DrawableElevator> elevators = new LinkedList<>();
    private final LinkedList<DrawableCustomer> customers = new LinkedList<>();

    private final LinkedList<Floor> floors = new LinkedList<>();
    private final LinkedList<FlyingText> flyingTexts = new LinkedList<>();


    { // tmp code to create elevators, in normal world they are created by Server
        var elevator = new LinkedList<Elevator>();
        for (int i = 0; i < 16; i++) {
            elevator.add(new Elevator(new ElevatorSystemSettings()));
        }
        var wallSize = ((double) settings.BUILDING_SIZE.y) / settings.FLOORS_COUNT;
        double distanceBetweenElevators = ((double) settings.BUILDING_SIZE.x) / (settings.ELEVATORS_COUNT + 1);
        for (int i = 0; i < 16; i++) {
            if (i < settings.ELEVATORS_COUNT) {
                elevator.get(i).setWallSize(wallSize);
                if (elevator.get(i).isVisible()) {
                    elevator.get(i).setPosition(new Vector2D(
                            distanceBetweenElevators * (i + 1), elevator.get(i).getPosition().y));
                } else {
                    elevator.get(i).setVisible(true);
                    elevator.get(i).setPosition(new Vector2D(distanceBetweenElevators * (i + 1), 0));
                }
            } else {
                elevator.get(i).setVisible(false);
            }
        }

        for (int i = 0; i < settings.ELEVATORS_COUNT; i++) {
            var newElevator = new DrawableElevator(
                    elevator.get(i),
                    settings.ELEVATOR_OPEN_CLOSE_TIME,
                    COLOR_SETTINGS.ELEVATOR_BACKGROUND_COLOR, COLOR_SETTINGS.ELEVATOR_DOOR_COLOR,
                    COLOR_SETTINGS.ELEVATOR_BORDER_COLOR
            );
            newElevator.setVisible(true);
            elevators.add(newElevator);
        }

        customers.add(new DrawableCustomer(new Customer(2, 3, new Vector2D(32, 2), 4, new Point(30, 30)),
                COLOR_SETTINGS.CUSTOMER_SKIN_COLOR));
        initialiseFirstData();
    }

    public Double getWallHeight() {
        return settings.BUILDING_SIZE.y / settings.FLOORS_COUNT;
    }

    public Double getDistanceBetweenElevators() {
        return settings.BUILDING_SIZE.x / (settings.ELEVATORS_COUNT + 1);
    }

    private void initialiseFirstData() {
        for (int i = 0; i < settings.FLOORS_COUNT; i++) {
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

    public void addMovingDrawable(FlyingText text) {
        flyingTexts.add(text);
    }

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

}
