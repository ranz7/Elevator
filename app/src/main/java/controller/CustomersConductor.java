package controller;

import connector.Gates;
import databases.LocalObjectsDatabase;
import databases.configs.CustomerConfig;
import lombok.Getter;
import architecture.tickable.Tickable;
import lombok.RequiredArgsConstructor;
import model.objects.customer.StandartCustomer.StandartCustomer;
import tools.Vector2D;
import model.AppModel;
import model.objects.customer.StandartCustomer.StandartCustomerState;

import java.util.Random;

import tools.Timer;

/**
 * Manipulate all customers in game.
 *
 * @see CustomerConfig
 */
@RequiredArgsConstructor
public class CustomersConductor implements Tickable {
    @Getter
    private final LocalObjectsDatabase dataBase;
    private final Gates gates;
    private final Timer spawnTimer = new Timer();
    private AppModel appModel;


    @Override
    public void tick(double deltaTime) {
        spawnTimer.tick(deltaTime);

        if (spawnTimer.isReady() && gates.appModel.customers.size() < settings.maxCustomers) {
            var maxFloor = ELEVATOR_SYSTEM_CONTROLLER.getSettings().floorsCount;
            var randomStartFloor = new Random().nextInt(0, maxFloor);
            var randomEndFloor = new Random().nextInt(0, maxFloor);
            randomEndFloor = (maxFloor + randomStartFloor + randomEndFloor - 1) % maxFloor;
            CreateCustomer(randomStartFloor, randomEndFloor);

            spawnTimer.restart(settings.spawnRate);
        }

        for (var customer : gates.appModel.customers) {
            customer.tick(deltaTime);
        }
    }

    public void CreateCustomer(int floorStart, int floorEnd) {
        double speed = new Random().nextDouble(
                settings.customerSpeed
                        - settings.customerSpeed / 3,
                settings.customerSpeed
                        + settings.customerSpeed / 3);
        var startPosition = getStartPositionForCustomer(floorStart);
        var customer = new StandartCustomer(floorStart, floorEnd, startPosition, speed, settings.customerSize);

        customer.setState(StandartCustomerState.GO_TO_BUTTON);
        gates.appModel.customers.add(customer);
    }

    private Vector2D getStartPositionForCustomer(int floorStart) {
        Vector2D startPosition = gates.appModel.getBuilding().getStartPositionAfterBuilding(floorStart);
        // So u can't see customer when he spawns
        if (startPosition.x == 0) {
            startPosition.x -= settings.customerSize.x * 2;
        } else {
            startPosition.x += settings.customerSize.x * 2;
        }
        return startPosition;
    }

    public void setModel(AppModel appModel) {
        this.appModel = appModel;
    }
}
