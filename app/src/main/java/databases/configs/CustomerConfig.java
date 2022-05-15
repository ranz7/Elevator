package databases.configs;

import controller.CustomersConductor;
import tools.Vector2D;

import java.io.Serializable;

/**
 * Info be used in customers creation
 *
 * @see CustomersConductor
 */
public class CustomerConfig implements Serializable {
    public final long timeToWaitAfterButtonClick = 1000;
    public final Vector2D customerSize = new Vector2D(30, 50);
    public final double slowSpeedMultiply = 0.5;
    public final double fastSpeedMultiply = 1.5;
    public final double customerSpeed = 100;
    public final long timeToWalk = 3000;
    public final int maxCustomers = 1;
    public final long spawnRate = 500;
}
