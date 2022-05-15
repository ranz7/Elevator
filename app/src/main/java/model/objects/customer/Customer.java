package model.objects.customer;

import model.objects.customer.StandartCustomer.StandartCustomerState;
import model.objects.movingObject.MovingCreature;
import model.objects.elevator.Elevator;
import lombok.Setter;
import lombok.Getter;
import model.objects.movingObject.trajectory.SpeedFunction;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Timer;
import tools.Vector2D;

@Getter
public abstract class Customer extends MovingCreature {
    public Customer(Vector2D position, Vector2D size, Trajectory trajecotry) {
        super(position, size, trajecotry);
    }
}
