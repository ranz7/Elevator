package model.objects.movingObject.trajectory;

public abstract class SpeedFunction {

    public static SpeedFunction WithConstantSpeed(double speed) {
        return new SpeedFunction() {

            @Override
            public boolean isConstFunction() {
                return true;
            }

            @Override
            public double tickAndGet(double deltaTime) {
                return speed;
            }
        };
    }

    public static SpeedFunction Momentarily() {
        return new SpeedFunction() {

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


    public abstract boolean isConstFunction();

    public abstract double tickAndGet(double deltaTime);
}
