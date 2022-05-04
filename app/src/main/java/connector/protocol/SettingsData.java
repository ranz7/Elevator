package connector.protocol;

import common.Vector2D;

import java.awt.Point;
import java.io.Serializable;

/**
 * Contains all information about application
 * <p>
 * This object need to be sent for client first, so that the client can initialize his database
 * according to this fields.
 * </p>
 */

public class SettingsData implements Serializable {
    public final Vector2D BUILDING_SIZE = new Vector2D(1000, 1800);
    public final Point ELEVATOR_SIZE = new Point(50, 80);
    public final Point CUSTOMER_SIZE = new Point(30, 50);
    public final long ELEVATOR_OPEN_CLOSE_TIME = 3500;
    public final int ELEVATORS_COUNT = 4;
    public final int FLOORS_COUNT = 15;
    public final double BUTTON_RELATIVE_POSITION =  7;
    public final double GAME_SPEED = 0;
    public final double VERSION = 0;

    // CONSTTRUCTOR
}
