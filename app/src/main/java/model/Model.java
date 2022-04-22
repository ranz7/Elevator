package model;

import model.objects.building.Building;
import model.objects.customer.Customer;

import java.util.LinkedList;
import java.util.stream.Collectors;

/*
 * Class to store all objects.
 */
public class Model {
    private Building building;
    public final LinkedList<Customer> CUSTOMERS = new LinkedList<>();

    public void Initialize(Building building) {
        this.building = building;
    }
}
