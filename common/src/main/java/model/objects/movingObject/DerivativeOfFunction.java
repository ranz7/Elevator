package model.objects.movingObject;

public abstract class DerivativeOfFunction {
    public DerivativeOfFunction(double derivative) {
        this.derivative = derivative;
    }

    protected double derivative;

    public abstract double getValue();

    public abstract boolean isConstFunction();

    public abstract double tickAndGet(double deltaTime);
}
