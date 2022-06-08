package settings;

import controller.subControllers.CustomersController;
import tools.Vector2D;

import java.io.Serializable;

/**
 * Info be used in customers creation
 *
 * @see CustomersController
 */

 class CustomerConfig implements Serializable {
    public final long timeToWaitAfterButtonClick = 1000;
    public final Vector2D customerSize = new Vector2D(40, 75);
    public final double slowSpeedMultiply = 0.7;
    public final double fastSpeedMultiply = 1.5;
    public double customerSpeedCoefficient = 2.3;

    public final double customerSpeed = 140;
    public final long timeToWalk = 3000;
    public final int maxCustomers = 5;
    public final long spawnRate = 500;
}
