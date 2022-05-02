package model;


import connector.protocol.SettingsData;
import controller.elevatorSystemController.ElevatorSystemSettings;
import drawable.ColorSettings;
import drawable.Drawable;
import drawable.drawableObjects.*;
import drawable.drawableObjects.Button;
import lombok.Getter;
import model.objects.customer.Customer;
import model.objects.elevator.Elevator;
import model.objects.movingObject.MovingObject;
import tools.Vector2D;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;


public class WindowModel {
    public final ColorSettings COLOR_SETTINGS = new ColorSettings();

    @Getter
    private SettingsData settings = new SettingsData();

    private LinkedList<DrawableElevator> elevators = new LinkedList<>();
    private LinkedList<DrawableCustomer> customers = new LinkedList<>();

    private LinkedList<Button> buttons = new LinkedList<>();
    private LinkedList<ElevatorBorder> border = new LinkedList<>();
    private LinkedList<BlackSpace> blackSpaces = new LinkedList<>();
    private LinkedList<FlyingText> flyingTexts = new LinkedList<>();
    private LinkedList<HidingWall> hidingWall = new LinkedList<>();
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

    private void initialiseFirstData() {
        var wallSize = settings.BUILDING_SIZE.y / settings.FLOORS_COUNT;
        double distanceBetweenElevators = ((double) settings.BUILDING_SIZE.x)
                / (settings.ELEVATORS_COUNT + 1);
        for (int i = 0; i < settings.FLOORS_COUNT; i++) {
            hidingWall.add(new HidingWall(
                    new Vector2D(settings.BUILDING_SIZE.x / 2., wallSize * i + settings.ELEVATOR_SIZE.y),
                    new Point((int) settings.BUILDING_SIZE.x, (int) (wallSize - settings.ELEVATOR_SIZE.y)),
                    COLOR_SETTINGS.WALL_COLOR
            ));
            for (int j = 0; j < settings.ELEVATORS_COUNT; j++) {
                buttons.add(new Button(new Vector2D(
                        distanceBetweenElevators * (j + 1) + settings.BUTTON_RELATIVE_POSITION,
                        i * wallSize + settings.CUSTOMER_SIZE.y + 4),
                        new Point(5, 5),
                        COLOR_SETTINGS.BUTTON_ON_COLOR, COLOR_SETTINGS.BUTTON_OF_COLOR));

                border.add(new ElevatorBorder(
                        new Vector2D(distanceBetweenElevators * (j + 1), i * wallSize),
                        elevators.get(j),
                        (int) wallSize, COLOR_SETTINGS.BORDER_COLOR, COLOR_SETTINGS.NUMBER_COLOR));

                blackSpaces.add(new BlackSpace(
                        new Vector2D(distanceBetweenElevators * (j + 1), i * wallSize),
                        elevators.get(j), COLOR_SETTINGS.BLACK_SPACE_COLOR, border.get(0).BORDER_SIZE));
            }
        }
    }

    public LinkedList<Drawable> getDrawableOjects() {
        LinkedList<Drawable> drawables = new LinkedList<>();
        drawables.addAll(blackSpaces);
        drawables.addAll(elevators);

        drawables.addAll(getElevatorDoors());
        drawables.addAll(hidingWall);
        drawables.addAll(border);
        drawables.addAll(buttons);
        drawables.addAll(customers.stream().toList());
        return drawables;
    }
    public LinkedList<Drawable> getDrawableOjectsHightPriority() {
        LinkedList<Drawable> drawables = new LinkedList<>();
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

    private Collection<Drawable> getElevatorDoors() {
        LinkedList<Drawable> elevatorDoors = new LinkedList<>();
        elevators.forEach(elevator -> elevatorDoors.add(elevator.DOORS));
        return elevatorDoors;
    }
}
