package model.objects;

import configs.RoomPrepareCompactData;
import controller.TickableList;
import gates.Gates;
import controller.subControllers.CustomersController;
import controller.subControllers.ElevatorsController;
import lombok.Getter;
import lombok.Setter;
import model.DatabaseOf;
import model.Transport;
import model.objects.customer.Customer;
import model.objects.customer.StandartCustomer.StandartCustomer;
import model.objects.elevator.Elevator;
import model.objects.floor.ElevatorButton;
import model.objects.floor.FloorStructure;
import model.objects.floor.Painting;
import protocol.Protocol;
import protocol.special.CreatureData;
import protocol.special.CreatureType;
import protocol.special.GameMapCompactData;
import settings.LocalCreaturesSettings;
import tools.Trio;
import tools.Vector2D;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import static configs.ConnectionSettings.VERSION;


public class GameMap extends Creature implements Transport<Creature> {
    @Getter
    private final int roomId;
    @Getter
    @Setter
    double gameSpeed = 1;

    @Getter
    private final DatabaseOf<Creature> localDataBase = new DatabaseOf<>(this, FloorStructure.class);
    @Getter
    private final LocalCreaturesSettings localCreaturesSettings;

    private final LinkedList<ElevatorsController> elevatorsControllers = new LinkedList<>();
    private final CustomersController customersController = new CustomersController(this);

    private final Gates controllerGates;

    public GameMap(Gates controllerGates, int roomId, LocalCreaturesSettings localCreaturesSettings) {
        super(new Vector2D(0, 0), new Vector2D(10, 10));
        this.controllerGates = controllerGates;
        this.roomId = roomId;
        this.localCreaturesSettings = localCreaturesSettings;

        for (int i = 0; i < 1; i++) {
            var floorStructure = new FloorStructure(new Vector2D(i * 500, 0), localCreaturesSettings);
            var elevatorController = new ElevatorsController(this, floorStructure);
             elevatorsControllers.add(elevatorController);
             floorStructure.fillWithElevators(elevatorController);
            floorStructure.fillWithPaintings();
            floorStructure.fillWithButtons(elevatorController);
            add(floorStructure);
            for (int j = 1; j < localCreaturesSettings.floorsCount(); j++) {
                floorStructure.addFloor();
            }
        }

    }

    @Override
    public void tick(double deltaTime) {
        localDataBase.tick(deltaTime);
        new TickableList(elevatorsControllers).tick(deltaTime);
        customersController.tick(deltaTime);
        localDataBase.removeIf(CreatureInterface::isDead);
    }

    public void send(Protocol protocol, Serializable data) {
        try {
            controllerGates.send(protocol, roomId, data);
        } catch (Gates.NobodyReceivedMessageException e) {
            setDead(true);
        }
    }

    public void moveCreatureInto(Integer moveCreatureId, Transport whereCreature) {
        localDataBase.moveCreatureInto(moveCreatureId, whereCreature);
    }

    public RoomPrepareCompactData createRoomData(double version) {
        return new RoomPrepareCompactData(version,
                getLocalCreaturesSettings().elevatorOpenCloseTime(),
                getLocalCreaturesSettings().customerSize(),
                getGameSpeed(),
                getRoomId());
    }

    public Serializable createCompactGameMapData() {
        var parentsAndCreatures = getLocalDataBase().streamTrio().map(Trio::getFirstAndThird);
        ArrayList<CreatureData> parentIdClassTypeObject = new ArrayList<>();
        parentsAndCreatures.forEach(
                parentAndCreature -> parentIdClassTypeObject.add(
                        new CreatureData(parentAndCreature.getSecond(),
                                parentAndCreature.getFirst(),
                                cast(parentAndCreature.getSecond().getClass()))
                )
        );
        return new GameMapCompactData(parentIdClassTypeObject, createRoomData(VERSION));
    }

    private CreatureType cast(Class<? extends Creature> aClass) {
        if (aClass == Elevator.class) {
            return CreatureType.ELEVATOR;
        }
        if (aClass == Customer.class) {
            return CreatureType.CUSTOMER;
        }
        if (aClass == ElevatorButton.class) {
            return CreatureType.ELEVATOR_BUTTON;
        }
        if (aClass == Painting.class) {
            return CreatureType.FLOOR_PAINTING;
        }
        if (aClass == FloorStructure.class) {
            return CreatureType.FLOOR;
        }
        if (aClass == GameMap.class) {
            return CreatureType.GAME_MAP;
        }
        if (aClass == StandartCustomer.class) {
            return CreatureType.CUSTOMER;
        }
        throw new RuntimeException("UNKNOW CLASS" + aClass.getName());
    }

    @Override
    public void add(Creature creature) {
        localDataBase.addCreature(creature);
    }
}
