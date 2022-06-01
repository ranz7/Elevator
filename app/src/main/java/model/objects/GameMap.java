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
import model.objects.floor.FloorStructure;
import protocol.Protocol;
import protocol.ProtocolMessage;
import protocol.special.GameMapCompactData;
import settings.LocalCreaturesSettings;
import tools.Vector2D;

import java.io.Serializable;
import java.util.LinkedList;


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
            //elevatorsControllers.add(elevatorController);
            //floorStructure.fillWithElevators(elevatorController);
            //floorStructure.fillWithPaintings();
            add(floorStructure);
            for (int j = 1; j < localCreaturesSettings.floorsCount(); j++) {
                floorStructure.addFloor();
            }
        }

    }

    @Override
    public void tick(double deltaTime) {
        //new TickableList(elevatorsControllers).tick(deltaTime);
        //customersController.tick(deltaTime);
        //localDataBase.tick(deltaTime);
        //localDataBase.removeIf(Creature::isDead);
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

    public RoomPrepareCompactData.RoomData toRoomData() {
        return new RoomPrepareCompactData.RoomData(
                getLocalCreaturesSettings().elevatorOpenCloseTime(),
                getLocalCreaturesSettings().customerSize(),
                getGameSpeed(),
                getRoomId());
    }

    public Serializable createCompactGameMapData() {
        return new GameMapCompactData(getLocalDataBase().toIdAndCreaturesList());
    }

    @Override
    public void add(Creature creature) {
        localDataBase.addCreature(creature);
    }
}
