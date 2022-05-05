package configs;

import controller.CustomersConductor;
import tools.Vector2D;

import java.awt.*;
import java.io.Serializable;

/**
 * Info be used in customers creation
 *
 * @see CustomersConductor
 */
public class CustomerSettings implements Serializable {
    public final long TIME_TO_WAIT_AFTER_BUTTON_CLICK = 1000;
    public final Vector2D CUSTOMER_SIZE = new Vector2D(30, 50);
    public final double SLOW_SPEED_MULTIPLY = 0.5;
    public final double FAST_SPEED_MULTIPLY = 1.5;
    public final double CUSTOMER_SPEED = 100;
    public final long TIME_TO_WALK = 3000;
    public final int MAX_CUSTOMERS = 2;
    public final long SPAWN_RATE = 500;
}
