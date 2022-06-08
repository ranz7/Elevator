package tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class MathFunctions {
    private static List<Function<Double, Double>> functions = new ArrayList<>();

    public static Function<Double, Double> sqrtSlow() {
        return (x -> Math.pow(x, 0.7));
    }

    public static Function<Double, Double> exponential() {
        return (x -> x / (Math.pow(Math.E, x - 1)));
    }

    public static Function<Double, Double> withSmallAtTheEndBump() {
        return (x -> {
            double bump = -(Math.pow(x - 0.4, 2) * 10) + 0.5;
            bump += Math.abs(bump);
            bump /= 6;
            return x / (Math.pow(Math.E, x - 1)) + bump;
        });
    }

    public static Function<Double, Double> withSmallAtTheEndBump2() {
        return (x -> {
            double bump = -(Math.pow(x - 0.8, 2) * 10) + 0.4;
            bump += Math.abs(bump);
            bump /= 5;
            return x / (Math.pow(Math.E, x - 1)) - bump;
        });
    }

    public static Function<Double, Double> withTwoBumps() {
        return (x -> {
            double bump = -(Math.pow(x - 0.8, 2) * 10) + 0.4;
            bump += Math.abs(bump);
            bump /= 5;

            double bump2 = -(Math.pow(x - 0.3, 2) * 9) + 0.7;
            bump2 += Math.abs(bump2);
            bump2 /= 9;
            return x / (Math.pow(Math.E, x - 1)) - bump + bump2;
        });
    }

    static {
        functions.add(sqrtSlow());
        functions.add(withSmallAtTheEndBump());
        functions.add(withSmallAtTheEndBump2());
        functions.add(exponential());
        functions.add(withTwoBumps());
    }

    public static Function<Double, Double> getFunction(int k) {
        return functions.get(k);
    }

    public static Function<Double, Double> randomFunction() {
        return functions.get(new Random().nextInt( functions.size()-1));
    }
}
