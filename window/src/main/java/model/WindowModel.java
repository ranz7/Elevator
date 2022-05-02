package model;


import connector.protocol.SettingsData;
import controller.elevatorSystemController.ElevatorSystemSettings;
import drawable.ColorSettings;
import drawable.DrawSettings;
import drawable.Drawable;
import drawable.drawableObjects.*;
import drawable.drawableObjects.building.floor.Floor;
import drawable.drawableObjects.building.BuildingWall;
import drawable.drawableObjects.building.HidingWall;
import drawable.drawableObjects.building.floor.Button;
import drawable.drawableObjects.customer.DrawableCustomer;
import drawable.drawableObjects.elevator.DrawableElevator;
import drawable.drawableObjects.building.floor.ElevatorBorder;
import lombok.Getter;
import model.objects.customer.Customer;
import model.objects.elevator.Elevator;
import model.objects.movingObject.MovingObject;
import tools.Vector2D;

import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;


public class WindowModel {
    public final ColorSettings COLOR_SETTINGS = new ColorSettings();
    public final DrawSettings DRAW_SETTINGS = new DrawSettings();

    @Getter
    private SettingsData settings = new SettingsData();

    private LinkedList<DrawableElevator> elevators = new LinkedList<>();
    private LinkedList<DrawableCustomer> customers = new LinkedList<>();

    private BuildingWall buildingWall = new BuildingWall(this);
    private Floor Floor = new Floor(this);

    private LinkedList<ElevatorBorder> border = new LinkedList<>();
    private LinkedList<HidingWall> hidingWall = new LinkedList<>();

    private LinkedList<FlyingText> flyingTexts = new LinkedList<>();

    private boolean needToInitialize = true;

    { // tmp code
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

    private void initialiseFirstData() { // to much code , need to be moved somewhere
        for (int i = 0; i < settings.FLOORS_COUNT; i++) {
            hidingWall.add(new HidingWall(
                    new Vector2D(settings.BUILDING_SIZE.x / 2., getWallHeight() * i + settings.ELEVATOR_SIZE.y),
                    new Point((int) settings.BUILDING_SIZE.x, (int) (getWallHeight() - settings.ELEVATOR_SIZE.y)),
                    COLOR_SETTINGS.WALL_COLOR
            ));
            for (int j = 0; j < settings.ELEVATORS_COUNT; j++) {
                var elevatorBorderPosition = new Vector2D(getDistanceBetweenElevators() * (j + 1), i * getWallHeight());
                border.add(new ElevatorBorder(elevatorBorderPosition, elevators.get(j), this));
            }
        }
    }

    public LinkedList<Drawable> getDrawableOjects() {
        LinkedList<Drawable> drawables = new LinkedList<>();
        drawables.add(buildingWall);
        elevators.forEach(drawable -> drawables.addAll(drawable.getDrawables()));
        border.forEach(drawable -> drawables.addAll(drawable.getDrawables()));
        drawables.addAll(hidingWall);
        drawables.addAll(customers.stream().toList());
        drawables.add(Floor);
        drawables.addAll(flyingTexts);
        drawables.sort(Comparator.comparingInt(Drawable::GetDrawPrioritet));
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
        border.forEach(elevatorBorder -> buttons.add(elevatorBorder.getButton()));

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
