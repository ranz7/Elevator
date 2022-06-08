package controller.subControllers;

import controller.Tickable;
import protocol.Protocol;
import lombok.RequiredArgsConstructor;
import model.objects.GameMap;
import model.objects.floor.FloorStructure;
import model.objects.customer.Customer;
import model.objects.customer.StandartCustomer.StandartCustomer;

import java.util.Random;

import model.objects.elevator.Elevator;
import settings.LocalCreaturesSettings;
import tools.Timer;

/**
 * Manipulate all customers in game.
 */
@RequiredArgsConstructor
public class CustomersController implements Tickable {
    private final GameMap gameMap;
    private final LocalCreaturesSettings settings;
    private final Timer spawnTimer = new Timer(1);

    @Override
    public void tick(double deltaTime) {
        spawnTimer.tick(deltaTime);
        spawn();
    }

    private void spawn() {
        var dataBase = gameMap.getLocalDataBase();
        var localCreaturesSettings = gameMap.getLocalCreaturesSettings();

        if (spawnTimer.isReady() && dataBase.countOf(StandartCustomer.class) < localCreaturesSettings.maxCustomers()) {

            var listOfBottomFloors = dataBase.streamOf(FloorStructure.class).filter(FloorStructure::isBottomFloor).toList();
            var randBottomFloor = listOfBottomFloors.get(new Random().nextInt(listOfBottomFloors.size()));

            var maxFloor = randBottomFloor.getHieght();

            var randomStartFloorNum = new Random().nextInt(0, maxFloor);
            var randomEndFloorNum = new Random().nextInt(0, maxFloor);
            randomEndFloorNum = (maxFloor + randomStartFloorNum + randomEndFloorNum - 1) % maxFloor;
            CreateCustomer(randBottomFloor.getUpperFloor(randomStartFloorNum), randBottomFloor.getUpperFloor(randomEndFloorNum));

            spawnTimer.restart(localCreaturesSettings.spawnTime());
        }
    }

    public void CreateCustomer(FloorStructure startFloor, FloorStructure endFloor) {
        double startPosition = startFloor.getStartPositionAfterBuilding();
        var customer = new StandartCustomer(endFloor, startPosition, this, gameMap.getLocalCreaturesSettings());
        startFloor.addCustomer(customer);
    }

    public void CreateCustomer(FloorStructure startFloor, FloorStructure endFloor, boolean left) {
        double startPosition = 0;

        if (left) {
            startPosition = 0 - settings.customerSize().x;
        } else {
            startPosition = endFloor.getSize().x + settings.customerSize().x;

        }
        var customer = new StandartCustomer(endFloor, startPosition, this, gameMap.getLocalCreaturesSettings());
        startFloor.addCustomer(customer);
    }

    public void customerGetIntoElevator(StandartCustomer standartCustomer, Elevator closestOpenedElevator) {
        gameMap.moveCreatureInto(standartCustomer.getId(), closestOpenedElevator);
        gameMap.send(Protocol.CUSTOMER_GET_IN_OUT, standartCustomer.getId());
    }

    public void customerGetOutFromElevator(StandartCustomer standartCustomer, Elevator elevator) {
        elevator.removeCustomer();
        gameMap.moveCreatureInto(standartCustomer.getId(), elevator.getCurrentFloor());
        gameMap.send(Protocol.CUSTOMER_GET_IN_OUT, standartCustomer.getId());
    }

    public void createCustomer(Integer floorId, Boolean ifLeft) {

        var dataBase = gameMap.getLocalDataBase();

        var listOfBottomFloors = dataBase.streamOf(FloorStructure.class).filter(FloorStructure::isBottomFloor).toList();
        var randBottomFloor = listOfBottomFloors.get(new Random().nextInt(listOfBottomFloors.size()));

        var maxFloor = randBottomFloor.getHieght();

        var randomStartFloorNum = ((FloorStructure) dataBase.get(floorId).getSecond()).getCurrentFloorNum();
        var randomEndFloorNum = new Random().nextInt(0, maxFloor);

        randomEndFloorNum = (maxFloor + randomStartFloorNum + randomEndFloorNum - 1) % maxFloor;
        CreateCustomer(randBottomFloor.getUpperFloor(randomStartFloorNum), randBottomFloor.getUpperFloor(randomEndFloorNum), ifLeft);
    }
}
