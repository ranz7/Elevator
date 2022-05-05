package configs;

import lombok.AllArgsConstructor;
import tools.Vector2D;

import java.io.Serializable;

/**
 * Contains all information about application
 * <p>
 * This object need to be sent for client first, so that the client can initialize his database
 * according to this fields.
 * </p>
 */

@AllArgsConstructor
public class MainInitializationSettings implements Serializable {
    public final Vector2D BUILDING_SIZE;
    public final Vector2D ELEVATOR_SIZE;
    public final Vector2D CUSTOMER_SIZE;
    public final long ELEVATOR_OPEN_CLOSE_TIME;
    public final int ELEVATORS_COUNT;
    public final int FLOORS_COUNT;
    public final double BUTTON_RELATIVE_POSITION;
    public final double GAME_SPEED;
    public final double VERSION;

}
