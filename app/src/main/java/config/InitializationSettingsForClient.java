package config;

import common.tools.Vector2D;

import java.awt.Point;
import java.io.Serializable;

/**
 * Contains all information about application
 * <p>
 * This object need to be sent for client first, so that the client can initialize his database
 * according to this fields.
 * </p>
 */

public class InitializationSettingsForClient implements Serializable {
    public final Vector2D BUILDING_SIZE;
    public final Point ELEVATOR_SIZE;
    public final Point CUSTOMER_SIZE;
    public final long ELEVATOR_OPEN_CLOSE_TIME;
    public final int ELEVATORS_COUNT;
    public final int FLOORS_COUNT;
    public final double BUTTON_RELATIVE_POSITION;
    public final double GAME_SPEED;
    public final double VERSION;

    public InitializationSettingsForClient(ElevatorSystemSettings settingsElevator, CustomerSettings settingsCustomer, double gameSpeed) {
        BUTTON_RELATIVE_POSITION = settingsElevator.BUTTON_RELATIVE_POSITION;
        ELEVATOR_OPEN_CLOSE_TIME = settingsElevator.ELEVATOR_OPEN_CLOSE_TIME;
        BUILDING_SIZE = new Vector2D(settingsElevator.BUILDING_SIZE);
        ELEVATOR_SIZE = settingsElevator.ELEVATOR_SIZE;
        CUSTOMER_SIZE = settingsCustomer.CUSTOMER_SIZE;
        FLOORS_COUNT = settingsElevator.FLOORS_COUNT;
        VERSION = ConnectionSettings.VERSION;
        GAME_SPEED = gameSpeed;
        ELEVATORS_COUNT = settingsElevator.getElevatorsCount();
    }
}
