package model;


import connector.protocol.SettingsData;
import controller.elevatorSystemController.ElevatorSystemSettings;
import drawable.ColorSettings;
import drawable.DrawSettings;
import drawable.Drawable;
import drawable.drawableObjects.*;
import drawable.drawableObjects.building.floor.Floor;
import drawable.drawableObjects.building.floor.Button;
import drawable.drawableObjects.customer.DrawableCustomer;
import drawable.drawableObjects.elevator.DrawableElevator;
import lombok.Getter;
import model.objects.customer.Customer;
import model.objects.elevator.Elevator;
import model.objects.movingObject.MovingObject;
import tools.Vector2D;

import java.awt.*;
import java.util.LinkedList;


public class GuiModel {
    public final ColorSettings COLOR_SETTINGS = new ColorSettings();
    public final DrawSettings DRAW_SETTINGS = new DrawSettings();
    @Getter
    private final SettingsData settings = new SettingsData();

    @Getter
    private final LinkedList<DrawableElevator> elevators = new LinkedList<>();
    private final LinkedList<DrawableCustomer> customers = new LinkedList<>();
    private final LinkedList<FlyingText> flyingTexts = new LinkedList<>();
    private final LinkedList<Floor> floors = new LinkedList<>();


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
                    COLOR_SETTINGS.ELEVATOR_BACKGROUND_COLOR, COLOR_SETTINGS.DOORS_COLOR,
                    COLOR_SETTINGS.BORDER_COLOR
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
        return ((double) settings.BUILDING_SIZE.x) / (settings.ELEVATORS_COUNT + 1);
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


    public Button getNearestButton(Vector2D data) {
        LinkedList<Button> buttons = new LinkedList<>();
        floors.forEach(floor -> floor.getBorder().forEach(
                border -> buttons.add(border.getButton())));

        return buttons.stream()
                .reduce(null, (buttonA, buttonB) -> {
                    if (buttonA == null) {
                        return buttonB;
                    }
                    if (buttonB == null) {
                        return buttonA;
                    }
                    if (data.getNearest(buttonA.getPosition(), buttonB.getPosition())
                            .equals(buttonA.getPosition())) {
                        return buttonA;
                    }
                    return buttonB;
                });
    }

}
