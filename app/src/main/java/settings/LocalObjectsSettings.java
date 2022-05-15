package settings;

import tools.Vector2D;

public class LocalObjectsSettings {
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

    public int elevatorsCount() {
        return elevatorSystemConfig.elevatorsCount;
    }

    public double buttonRelativePosition() {
        return elevatorSystemConfig.buttonRelativePosition;
    }

    public double distanceBetweenElevators() {
        return (buildingSize().x) / (elevatorsCount() + 1);
    }

    public long picturesGeneratorSeed() {
        return elevatorSystemConfig.picturesGeneratorSeed;
    }
}
