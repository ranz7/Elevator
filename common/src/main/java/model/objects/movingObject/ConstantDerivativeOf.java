package model.objects.movingObject;

public class ConstantDerivativeOf extends DerivativeOfFunction {
    public ConstantDerivativeOf(double currentSpeed) {
        super(currentSpeed);
    }

    @Override
    public double getValue() {
        return derivative;
    }

    @Override
    public boolean isConstFunction() {
        return true;
    }

    @Override
    public double tickAndGet(double deltaTime) {
        return derivative;
    }
}
