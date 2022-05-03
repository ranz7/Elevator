package connector.protocol;

import connector.clientServer.ConnectionSettings;
import model.objects.movingObject.Creature;
import model.objects.customer.Customer;
import model.objects.elevator.Elevator;

import java.io.Serializable;
import java.util.LinkedList;


/**
 * Contains all data about creatures to send.
 * <p>
 * This data is sending SSPS times at second
 *
 * @see ConnectionSettings
 * </p>
 */

public class CreaturesData implements Serializable {
    public final LinkedList<Creature> CUSTOMERS = new LinkedList<>();
    public final LinkedList<Creature> ELEVATORS = new LinkedList<>();

    public CreaturesData(LinkedList<Customer> customers, LinkedList<Elevator> elevators) {
        customers.forEach(customer -> this.CUSTOMERS.add(new Creature(customer)));
        elevators.forEach(elevator -> this.ELEVATORS.add(new Creature(elevator)));
    }
}
