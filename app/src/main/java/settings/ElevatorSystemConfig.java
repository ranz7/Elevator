package settings;

import controller.subControllers.ElevatorsController;
import lombok.Getter;
import lombok.Setter;
import tools.Vector2D;

import java.io.Serializable;


/**
 * Info be used in elevator creation
 *
 * @see ElevatorsController
 */

class ElevatorSystemConfig implements Serializable {
    public Vector2D floorSize = new Vector2D(800, 160);
    public final long elevatorAfterCloseAfkTime = 500;
    public final long elevatorWaitAsOpenedTime = 3000;

    public Vector2D elevatorSize = new Vector2D(50, 80);

    public double buttonRelativePosition = elevatorSize.x / 2. + 7;
    public long elevatorOpenCloseTime = 1000;
    public final int elevatorMaxHumanCapacity = 4;
    public double elevatorSpeed = 150;
    public int floorsCount = 5;
    public Vector2D buttonSize = new Vector2D(5, 5);

    @Getter
    @Setter
    public int elevatorsCount = 5;
}
