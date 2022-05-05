package configs;

import controller.ElevatorsConductor;
import lombok.Getter;
import lombok.Setter;
import tools.Vector2D;

import java.io.Serializable;


/**
 * Info be used in elevator creation
 *
 * @see ElevatorsConductor
 */

public class ElevatorSystemSettings implements Serializable {
    public final Vector2D BUILDING_SIZE = new Vector2D(800, 896);
    public final long ELEVATOR_AFTER_CLOSE_AFK_TIME = 500;
    public final long ELEVATOR_WAIT_AS_OPENED_TIME = 3000;
    public final Vector2D ELEVATOR_SIZE = new Vector2D(50, 80);
    public final double BUTTON_RELATIVE_POSITION = ELEVATOR_SIZE.x / 2. + 7;
    public final long ELEVATOR_OPEN_CLOSE_TIME = 1000;
    public final int ELEVATOR_MAX_HUMAN_CAPACITY = 4;
    public final int MAX_ELEVATORS_COUNT = 16;
    public final double ELEVATOR_SPEED = 150;
    public final int FLOORS_COUNT = 6;

    @Getter
    @Setter
    public int elevatorsCount = 5;
}
