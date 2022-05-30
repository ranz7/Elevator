package settings;

import tools.Vector2D;

import java.util.Random;


public class LocalCreaturesSettings {
    private final CustomerConfig customerConfig = new CustomerConfig();
    private final ElevatorSystemConfig elevatorSystemConfig = new ElevatorSystemConfig();
    private final Random random = new Random();
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

    public long picturesGeneratorSeed() {
        return elevatorSystemConfig.picturesGeneratorSeed;
    }

    public long maxCustomers() {
        return customerConfig.maxCustomers;
    }

    // NOT SAVE
    public Vector2D floorSize() {
        return elevatorSystemConfig.floorSize;
    }
    // NOT SAVE
    public double distanceBetweenElevators() {
        return (floorSize().x) / (elevatorsCount() + 1);
    }

    public Vector2D buttonSize() {
        return elevatorSystemConfig.buttonSize;
    }

    public long spawnTime() {
        return customerConfig.spawnRate;
    }

    public double customerSpeed() {
        return customerConfig.customerSpeed;
    }

    public double customerSpeedRangeCoefficient() {
        return customerConfig.customerSpeedCoefficient;
    }

    public double customerRandomSpeed() {
        return random.nextDouble(
                customerSpeed() - customerSpeed() / customerSpeedRangeCoefficient(),
                customerSpeed() + customerSpeed() / customerSpeedRangeCoefficient());

    }

    public Double customerMaxSize() {
        return customerSize().x;
    }

    public double elevatorSpeed() {
        return elevatorSystemConfig.elevatorSpeed;
    }

    public long elevatorAfterCloseAfkTime() {
        return elevatorSystemConfig.elevatorAfterCloseAfkTime;
    }

    public long elevatorWaitAsOpenedTime() {
        return elevatorSystemConfig.elevatorWaitAsOpenedTime;
    }

    public long customerWaitAfterClick() {
        return customerConfig.timeToWaitAfterButtonClick;
    }

    public long customerTimeToWalk() {
        return customerConfig.timeToWalk;
    }

    public Double slowSpeedCustomerMultiply() {
        return null;
    }

    public long maxHumanCapacity() {
        return elevatorSystemConfig.elevatorMaxHumanCapacity;
    }
}
