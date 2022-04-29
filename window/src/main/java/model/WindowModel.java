package model;

import connector.protocol.SettingsData;
import drawable.ColorSettings;
import drawable.Drawable;
import drawable.drawableObjects.*;
import drawable.drawableObjects.Button;
import lombok.Getter;
import lombok.Setter;
import tools.Vector2D;

import java.awt.*;
import java.util.LinkedList;


public class WindowModel {
    public final ColorSettings COLOR_SETTINGS = new ColorSettings();

    @Getter
    private SettingsData settings = new SettingsData();
    @Getter
    @Setter
    private long lastServerRespondTime = 0;

    private LinkedList<DrawableCustomer> customers = new LinkedList<>();

    private LinkedList<HidingWall> hidingWall = new LinkedList<>();
    private LinkedList<Button> buttons = new LinkedList<>();

    private void initialiseFirstData() {
        var wallSize = settings.BUILDING_SIZE.y / settings.FLOORS_COUNT;
        double distanceBetweenElevators = ((double) settings.BUILDING_SIZE.x)
                / (settings.ELEVATORS_COUNT + 1);
        for (int i = 0; i < settings.FLOORS_COUNT; i++) {
            hidingWall.add(new HidingWall(
                    new Vector2D(settings.BUILDING_SIZE.x / 2., wallSize * i + settings.ELEVATOR_SIZE.y),
                    new Point(settings.BUILDING_SIZE.x, (wallSize - settings.ELEVATOR_SIZE.y)),
                    COLOR_SETTINGS.WALL_COLOR
            ));
            for (int j = 0; j < settings.ELEVATORS_COUNT; j++) {
                buttons.add(new Button(new Vector2D(
                        distanceBetweenElevators * (j + 1) + settings.BUTTON_RELATIVE_POSITION,
                        i * wallSize + settings.CUSTOMER_SIZE.y + 4),
                        new Point(5, 5),
                        COLOR_SETTINGS.BUTTON_ON_COLOR, COLOR_SETTINGS.BUTTON_OF_COLOR));

            }
        }
    }

    public LinkedList<Drawable> getDrawableOjects() {
        customers.forEach(elevator -> elevator.setServerRespondTime(lastServerRespondTime));
        LinkedList<Drawable> drawables = new LinkedList<>();

        drawables.addAll(hidingWall);
        drawables.addAll(buttons);
        drawables.addAll(customers.stream().filter(DrawableCustomer::isBehindElevator).toList());
        return drawables;
    }
}
