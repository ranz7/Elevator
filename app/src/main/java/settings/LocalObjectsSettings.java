package databases;

import tools.Vector2D;

public class LocalObjectsDatabase {
    private final CustomerConfig customerConfig = new CustomerConfig();
    private final ElevatorSystemConfig elevatorSystemConfig = new ElevatorSystemConfig();

    public Vector2D buildingSize() {
        return elevatorSystemConfig.buildingSize;
    }

    public Vector2D elevatorSize() {
        return elevatorSystemConfig.elevatorSize;
    }

    public Vector2D customerSize() {
        return customerConfig.customerSize;
    }

    public long elevatorOpenCloseTime() {
        return elevatorSystemConfig.elevatorOpenCloseTime;
    }

    public int floorsCount() {
        return elevatorSystemConfig.floorsCount;
    }

    public double buttonRelativePosition() {
        return elevatorSystemConfig.buttonRelativePosition;
    }
}
