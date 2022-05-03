package model;

import connector.protocol.CreaturesData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.objects.movingObject.MovingObject;
import model.objects.building.Building;
import model.objects.customer.Customer;

import java.util.LinkedList;
import java.util.stream.Collectors;

/*
 * Class to store all objects.
 */
@NoArgsConstructor
public class Model {
    @Getter
    @Setter
    private Building building;
    public final LinkedList<Customer> CUSTOMERS = new LinkedList<>();

    public void Initialize(Building building) {
        this.building = building;
    }

    public void clearDead() {
        CUSTOMERS.removeIf(MovingObject::isDead);
    }

    public CreaturesData getDataToSent() {
        return new CreaturesData(CUSTOMERS, building.ELEVATORS);
    }
}
