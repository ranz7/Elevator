package model.objects.movingObject.trajectory;

public abstract class SpeedFunction {
    public SpeedFunction(double speed) {
        this.speed = speed;
    }

    protected double speed;

    public static SpeedFunction WithConstantSpeed(double speed) {
        return new SpeedFunction(speed) {
            @Override
            public double getSpeed() {
                return speed;
            }

            @Override
            public boolean isConstFunction() {
                return true;
            }

            @Override
            public double tickAndGet(double deltaTime) {
                return getSpeed();
            }
        };
    }

    public static SpeedFunction Momentarily() {
        return new SpeedFunction(0) {
            @Override
            public double getSpeed() {
                return 9999;
            }

            @Override
            public boolean isConstFunction() {
                return true;
            }

            @Override
            public double tickAndGet(double deltaTime) {
                return 9999;
            }
        };
    }

    public abstract double getSpeed();

    public abstract boolean isConstFunction();

    public abstract double tickAndGet(double deltaTime);
}
