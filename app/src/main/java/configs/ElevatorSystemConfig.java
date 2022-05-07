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

public class ElevatorSystemConfig implements Serializable {
    public final Vector2D buildingSize = new Vector2D(800, 896);
    public final long elevatorAfterCloseAfkTime = 500;
    public final long elevatorWaitAsOpenedTime = 3000;
    public final Vector2D elevatorSize = new Vector2D(50, 80);
    public final double buttonRelativePosition = elevatorSize.x / 2. + 7;
    public final long elevatorOpenCloseTime = 1000;
    public final int elevatorMaxHumanCapacity = 4;
    public final int maxElevatorsCount = 16;
    public final double elevatorSpeed = 150;
    public final int floorsCount = 6;

    @Getter
    @Setter
    public int elevatorsCount = 5;
}
