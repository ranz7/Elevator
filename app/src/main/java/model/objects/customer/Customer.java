package model.objects.customer;

import model.objects.movingObject.MovingCreature;
import lombok.Getter;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Vector2D;

@Getter
public abstract class Customer extends MovingCreature {
    public Customer(Vector2D position, Vector2D size, Trajectory trajecotry) {
        super(position, size, trajecotry);
    }
}
