package connector.protocol;

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
    public final Point BUILDING_SIZE = new Point(0, 0);
    public final Point ELEVATOR_SIZE = new Point(0, 0);
    public final Point CUSTOMER_SIZE = new Point(0, 0);
    public final int ELEVATORS_COUNT = 0;
    public final int FLOORS_COUNT = 0;
    public final double GAME_SPEED = 0;
    public final double VERSION = 0;

    // CONSTTRUCTOR
}
