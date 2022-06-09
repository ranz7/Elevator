package settings;

import tools.Vector2D;

import java.util.Random;


public class LocalCreaturesSettings {
    private final CustomerConfig customerConfig = new CustomerConfig();
    private final ElevatorSystemConfig elevatorSystemConfig = new ElevatorSystemConfig();
    private final Random random = new Random();

    public LocalCreaturesSettings(Integer roomId) {
        var rand = new Random(roomId);
        elevatorSystemConfig.elevatorSize.set(
                elevatorSystemConfig.elevatorSize.add(Vector2D.Random(rand).multiply(new Vector2D(10, 20)))
        );
        customerConfig.slowSpeedMultiply *= 1 + (rand.nextDouble() - 0.5);
        customerConfig.fastSpeedMultiply *= 1 + (rand.nextDouble() - 0.5);
        elevatorSystemConfig.floorSize.set(
                elevatorSystemConfig.floorSize.add(Vector2D.Random(rand).multiply(new Vector2D(200, 50))));
        elevatorSystemConfig.floorsCount += (rand.nextInt(5 - 2));
        elevatorSystemConfig.elevatorsCount += (rand.nextInt(4 - 2));
        elevatorSystemConfig.elevatorSpeed *= 1 + (rand.nextDouble() - 0.5);

        customerConfig.timeToWalk *= 1 + (rand.nextDouble() - 0.5);
        customerConfig.spawnRate *= 1 + 2 * (rand.nextDouble() - 0.5);
        customerConfig.maxCustomers += (rand.nextInt(8 - 2));
        customerConfig.customerSpeed *= 1 + (rand.nextDouble() - 0.5);
    }

    public Vector2D elevatorSize() {
        return elevatorSystemConfig.elevatorSize;
    }

    public Vector2D customerSize() {
        return customerConfig.customerSize.multiply(
                new Vector2D(new Random().nextDouble() / 1.25 + 0.2,
                        new Random().nextDouble() / 1.5 + 0.5));
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

    public double spawnTime() {
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

    public double customerTimeToWalk() {
        return customerConfig.timeToWalk;
    }

    public Double slowSpeedCustomerMultiply() {
        return customerConfig.slowSpeedMultiply;
    }

    public long maxHumanCapacity() {
        return elevatorSystemConfig.elevatorMaxHumanCapacity;
    }

    public Double fastSpeedCustomerMultiply() {
        return customerConfig.fastSpeedMultiply;
    }
}
