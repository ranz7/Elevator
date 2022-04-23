package model;

import lombok.Getter;
import lombok.Setter;
import model.objects.building.Building;
import model.objects.customer.Customer;
import model.objects.movingObject.MovingObject;

import java.util.LinkedList;
/*
 * Class to store all objects.
 */
public class Model {
    @Getter @Setter
    private Building building;
    public final LinkedList<Customer> CUSTOMERS = new LinkedList<>();

    public void Initialize(Building building) {
        this.building = building;
    }

    public void clearDead() {
        CUSTOMERS.removeIf(MovingObject::isDead);
    }
}
