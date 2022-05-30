package model.objects.movingObject.trajectory;

import lombok.Setter;
import tools.Vector2D;

import java.util.function.Supplier;

public abstract class SpeedFunction {

    public static SpeedFunction WithConstantSpeed(Supplier<Double> speed) {
        return new SpeedFunction() {

            @Override
            public double tickAndGet(double deltaTime) {
                return speed.get() * speedCoefficient;
            }
        };
    }

    public static SpeedFunction Momentarily() {
        return new SpeedFunction() {

            @Override
            public double tickAndGet(double deltaTime) {
                return 9999;
            }
        };
    }

    @Setter
    double speedCoefficient = 1;

    public abstract double tickAndGet(double deltaTime);
}
