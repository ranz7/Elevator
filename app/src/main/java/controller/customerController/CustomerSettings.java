package controller.customerController;

import java.awt.*;
import java.io.Serializable;

/**
 * Info be used in customers creation
 *
 * @see CustomersController
 */
public class CustomerSettings implements Serializable {
    public final Point CUSTOMER_SIZE = new Point(30, 50);
    public final double CUSTOMER_SPEED = 100;
    public final int MAX_CUSTOMERS = 2;
    public final long SPAWN_RATE = 500;
}
