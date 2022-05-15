package model;

import configs.ConnectionSettings;
<<<<<<< Updated upstream
import configs.CustomerSettings;
import configs.ElevatorSystemSettings;
import configs.MainInitializationSettings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.objects.movingObject.Creature;
import model.objects.movingObject.CreaturesData;
import model.objects.movingObject.MovingObject;
import model.objects.building.Building;
import model.objects.customer.Customer;
import tools.tools.Vector2D;
=======
import model.objects.AppGameMap;
import settings.LocalObjectsSettings;
import configs.ConnectionEstalblishConfig;
import lombok.Getter;
import connector.protocol.GameMapCompactData;
>>>>>>> Stashed changes

import java.io.Serializable;

/*
 * Class to store all objects.
 */
<<<<<<< Updated upstream
@NoArgsConstructor
public class AppModel {
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

    public CreaturesData getDataToSent() {
        List<Creature> customersTmp = new LinkedList<>();
        List<Creature> elevatorsTmp = new LinkedList<>();
        customers.forEach(customer -> customersTmp.add(new Creature(customer)));
        building.ELEVATORS.forEach(elevator -> elevatorsTmp.add(new Creature(elevator)));
        return new CreaturesData(customersTmp, elevatorsTmp);
    }

    public Serializable createMainInitializationSettingsToSend(
            ElevatorSystemSettings settingsElevator, CustomerSettings settingsCustomer, double gameSpeed) {
        return new MainInitializationSettings(
                new Vector2D(settingsElevator.BUILDING_SIZE),
                settingsElevator.ELEVATOR_SIZE,
                settingsCustomer.CUSTOMER_SIZE,
                settingsElevator.ELEVATOR_OPEN_CLOSE_TIME,
                settingsElevator.getElevatorsCount(),
                settingsElevator.FLOORS_COUNT,
                settingsElevator.BUTTON_RELATIVE_POSITION,
=======

public class AppModel {
    @Getter
    private final LocalObjectsSettings localObjectsSettings = new LocalObjectsSettings();
    AppGameMap appGameMap = new AppGameMap(localObjectsSettings);

    public GameMapCompactData sendMap() {
        return new GameMapCompactData(appGameMap.getDataBase().toIdAndCreaturesList());
    }

    public Serializable createMainInitializationSettingsToSend(double gameSpeed) {
        return new ConnectionEstalblishConfig(
                localObjectsSettings.elevatorOpenCloseTime(),
                localObjectsSettings.buttonRelativePosition(),
>>>>>>> Stashed changes
                gameSpeed,
                ConnectionSettings.VERSION);
    }
}
