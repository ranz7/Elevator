package model.objects.floor;

import controller.subControllers.ElevatorsController;
import lombok.Getter;
import lombok.Setter;
import model.DatabaseOf;
import model.Transport;
import model.Transportable;
import model.objects.GameMap;
import model.objects.customer.StandartCustomer.StandartCustomer;
import settings.LocalCreaturesSettings;
import model.objects.Creature;
import model.objects.elevator.Elevator;
import tools.Vector2D;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class FloorStructure extends Creature implements Transport<Creature>, Transportable<Creature> {
    @Getter
    private final DatabaseOf<Creature> localDataBase = new DatabaseOf<>(this,
            FloorStructure.class, Elevator.class, Painting.class, ElevatorButton.class,StandartCustomer.class
    );

    @Getter
    @Setter
    private Transport<Creature> transport;

    private final LocalCreaturesSettings settings;
    static final private Random random = new Random();

    @Getter
    private FloorStructure floorStructureTop;
    @Getter
    private FloorStructure floorStructureBot;

    private FloorStructure(Vector2D position, LocalCreaturesSettings settings, FloorStructure floorStructureBot) {
        this(position, settings);
        this.floorStructureBot = floorStructureBot;
    }

    public FloorStructure(Vector2D position, LocalCreaturesSettings settings) {
        super(position, settings.floorSize());
        this.settings = settings;
        settings.distanceBetweenElevators();
    }

    public boolean isBottomFloor() {
        return floorStructureBot == null;
    }

    public void addFloor(ElevatorsController elevatorsController) {
        if (floorStructureTop != null) {
            floorStructureTop.addFloor(elevatorsController);
            return;
        }
        floorStructureTop = new FloorStructure(settings.floorSize().withX(0), settings, this);
        floorStructureTop.fillWithPaintings();
        floorStructureTop.fillWithButtons(elevatorsController);

        add(floorStructureTop);
    }

    public FloorStructure getUpperFloor(int numberOfFloor) {
        if (numberOfFloor == 0) {
            return this;
        }

        return floorStructureTop.getUpperFloor(numberOfFloor - 1);
    }

    public double getStartPositionAfterBuilding() {
        boolean spawnInLeftCorner = new Random().nextBoolean();
        if (spawnInLeftCorner) {
            return 0 - settings.customerSize().x;
        }
        return getSize().x+settings.customerSize().x;
    }

    public ElevatorButton getClosestButtonOnFloor(Vector2D position) {
        List<ElevatorButton> buttons = localDataBase.streamOfOnlyOwned(ElevatorButton.class).toList();
        ElevatorButton answer = null;
        Vector2D nearestPosition = new Vector2D(999, 999);
        for (var button : buttons) {
            var buttonPosition = button.getPosition();
            if (position.getNearest(nearestPosition, buttonPosition).equals(buttonPosition)) {
                nearestPosition = buttonPosition;
                answer = button;
            }
        }
        return answer;
    }

    public Elevator getClosestOpenedElevatorOnFloor(Vector2D position) {
        if (!isOpenedElevatorOnFloorExist()) {
            return null;
        }
        List<Elevator> elevators = localDataBase.streamOfOnlyOwned(Elevator.class).filter(Elevator::isOpened).toList();
        Elevator answer = null;
        Vector2D nearestPosition = new Vector2D(999, 999);
        for (var elevator : elevators) {
            var elevatorPosition = elevator.getPosition();
            if (position.getNearest(nearestPosition, elevatorPosition).equals(elevatorPosition)) {
                nearestPosition = elevatorPosition;
                answer = elevator;
            }
        }
        return answer;
    }

    private boolean isOpenedElevatorOnFloorExist() {
        return localDataBase.streamOfOnlyOwned(Elevator.class).filter(Elevator::isOpened).anyMatch(Elevator::isFree);
    }

    public int getHieght() {
        if (floorStructureTop == null) {
            return 1;
        }
        return 1 + floorStructureTop.getHieght();
    }

    public void addCustomer(StandartCustomer customer) {
        add(customer);
    }


    public int getCurrentFloorNum() {
        if (floorStructureBot == null) {
            return 0;
        }
        return floorStructureBot.getCurrentFloorNum() + 1;
    }

    private List<Double> getDefaultPositionOfElevators() {
        List<Double> list = new LinkedList<>();
        double distanceBetweenElevators = settings.distanceBetweenElevators();
        for (int j = 0; j < settings.elevatorsCount(); j++) {
            list.add(distanceBetweenElevators * j + distanceBetweenElevators );
        }
        return list;
    }

    public void fillWithElevators(ElevatorsController elevatorController, GameMap gameMap) {
        getDefaultPositionOfElevators().forEach(position -> {
            add(new Elevator(position, elevatorController,gameMap, settings));
        });
    }

    public void fillWithPaintings() {
        double distanceBetweenElevators = settings.distanceBetweenElevators();
        getDefaultPositionOfElevators().forEach(position -> {
            add(new Painting(new Vector2D(position- distanceBetweenElevators/2, settings.elevatorSize().y), random.nextInt()));
        });
    }

    public void fillWithButtons(ElevatorsController elevatorController) {
        getDefaultPositionOfElevators().forEach(position -> {
            add(new ElevatorButton(position,elevatorController, settings));
        });
    }

    @Override
    public void add(Creature creature) {
        localDataBase.addCreature(creature);
    }

}


