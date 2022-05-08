package model;

import architecture.tickable.TickableList;
import configs.ConnectionSettings;
import configs.CustomerConfig;
import configs.ElevatorSystemConfig;
import configs.ConnectionEstalblishConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.objects.Creature;
import model.objects.CreaturesData;
import model.objects.movingObject.MovingObject;
import model.objects.building.Building;
import model.objects.customer.Customer;
import tools.Vector2D;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/*
 * Class to store all objects.
 */
@NoArgsConstructor
public class AppModel implements Model {
    @Getter
    @Setter
    private Building building;
    public final LinkedList<Customer> customers = new LinkedList<>();

    public void Initialize(Building building) {
        this.building = building;
    }

    public void clearDead() {
        customers.removeIf(MovingObject::isDead);
    }

    @Override
    public void start() {}

    @Override
    public void update() {

    }


    @Override
    public TickableList getTickableList() {
        return new TickableList().add(customers).add(building.getElevators());
    }

    public CreaturesData getDataToSent() {
        List<Creature> customersTmp = new LinkedList<>();
        List<Creature> elevatorsTmp = new LinkedList<>();
        customers.forEach(customer -> customersTmp.add(new Creature(customer)));
        building.elevators.forEach(elevator -> elevatorsTmp.add(new Creature(elevator)));
        return new CreaturesData(customersTmp, elevatorsTmp);
    }

    public Serializable createMainInitializationSettingsToSend(
            ElevatorSystemConfig settingsElevator, CustomerConfig settingsCustomer, double gameSpeed) {
        return new ConnectionEstalblishConfig(
                new Vector2D(settingsElevator.buildingSize),
                settingsElevator.elevatorSize,
                settingsCustomer.customerSize,
                settingsElevator.elevatorOpenCloseTime,
                settingsElevator.getElevatorsCount(),
                settingsElevator.floorsCount,
                settingsElevator.buttonRelativePosition,
                gameSpeed,
                ConnectionSettings.VERSION);
    }

}
