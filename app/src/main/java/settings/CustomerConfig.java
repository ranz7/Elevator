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
    public double slowSpeedMultiply = 0.7;
    public double fastSpeedMultiply = 1.5;
    public double customerSpeedCoefficient = 2.3;

    public  double customerSpeed = 140;
    public  double timeToWalk = 3000;
    public  int maxCustomers = 7;
    public  double spawnRate = 500;
}
