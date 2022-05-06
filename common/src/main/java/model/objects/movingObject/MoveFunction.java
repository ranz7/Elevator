package model.objects.movingObject;

import architecture.tickable.Tickable;
import tools.Timer;
import tools.Vector2D;

public abstract class MoveFunction implements Tickable {
    protected double SPEED_COEFFICIENT = 1000;

    protected DerivativeOfFunction derivative;
    protected Vector2D position;
    protected Vector2D destination;

    /**
     * get Current position of function
     */
    public abstract Vector2D getPosition();

    /**
     * get estimation time of end function.
     * Jęśli czas jest podany - wrócić [ stała prędkość | zmienna prędkość ]
     * Jeśli funkcja umie dotrzeć sama - null (nieskonczonosc czasu) [ stała prędkość | zmienna prędkość ]
     * Jeśli funkcja nie podaje czasu i nie wie czy dotre sama - znalezc czas w ktorym objekt dotrze do punktu [ stała prędkość]
     */
    public abstract Timer getEstimationEndTime();

    public abstract boolean reached();

    public boolean isConstantSpeed() {
        return derivative.isConstFunction();
    }

    public double getDerivative() {
        return derivative.getValue();
    }
}
