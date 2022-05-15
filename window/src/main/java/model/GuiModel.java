package model;


<<<<<<< Updated upstream
import configs.MainInitializationSettings;
import configs.ColorSettings;
import configs.DrawSettings;
import drawable.drawableBase.creatureWithTexture.Drawable;
import drawable.drawableObjectsConcrete.*;
import drawable.drawableObjectsConcrete.building.floor.Floor;
import drawable.drawableObjectsConcrete.building.floor.elevator.ElevatorButton;
import drawable.drawableObjectsConcrete.customer.DrawableCustomer;
import drawable.drawableObjectsConcrete.elevator.DrawableElevator;
import lombok.Getter;
import model.objects.movingObject.Creature;
import model.objects.movingObject.CreaturesData;
import model.objects.movingObject.MovingObject;
import tools.tools.Vector2D;

import java.util.LinkedList;
import java.util.List;


public class GuiModel {
    // TODO REFACTOR - move to one SUPER SETINGS class
    public final ColorSettings COLOR_SETTINGS = new ColorSettings();
    public final DrawSettings DRAW_SETTINGS = new DrawSettings();
    @Getter
    private MainInitializationSettings settings;
    //

    //TODO move into DRAW CLIENT OBJECTS
    @Getter
    private final LinkedList<DrawableElevator> elevators = new LinkedList<>();
    private final LinkedList<DrawableCustomer> customers = new LinkedList<>();

    private final LinkedList<Floor> floors = new LinkedList<>();
    private final LinkedList<FlyingText> flyingTexts = new LinkedList<>();


    private boolean needToInitialize = true;


    public Double getWallHeight() {
        return settings.BUILDING_SIZE.y / settings.FLOORS_COUNT;
    }

    public Double getDistanceBetweenElevators() {
        return settings.BUILDING_SIZE.x / (settings.ELEVATORS_COUNT + 1);
    }

    private void initialiseFirstData() {
        for (int i = 0; i < settings.FLOORS_COUNT; i++) {
            floors.add(new Floor(i, this));
        }

        needToInitialize = false;
    }

    public LinkedList<Drawable> getDrawableOjects() {
        LinkedList<Drawable> drawables = new LinkedList<>();
        elevators.forEach(drawable -> drawables.addAll(drawable.getDrawables()));
        floors.forEach(drawable -> drawables.addAll(drawable.getDrawables()));
        drawables.addAll(customers.stream().toList());
        drawables.addAll(flyingTexts);
        return drawables;
    }

    public void addMovingDrawable(FlyingText text) {
        flyingTexts.add(text);
    }

    public void clearDead() {
        flyingTexts.removeIf(MovingObject::isDead);
=======
import connector.protocol.GameMapCompactData;
import settings.CombienedDrawSettings;
import configs.ConnectionEstalblishConfig;
import drawable.abstracts.DrawableMovingCreature;
import model.packageLoader.PackageLoader;
import drawable.concretes.building.floor.elevatorSpace.ElevatorButton;
import drawable.concretes.customer.DrawableCustomer;
import drawable.concretes.elevator.DrawableElevator;
import lombok.Getter;
import tools.Vector2D;

public class GuiModel extends DataBaseOfDrawableCreatures {
    @Getter
    private final CombienedDrawSettings combienedDrawSettings = new CombienedDrawSettings();

    public void addMovingDrawable(DrawableMovingCreature text) {
        add(text);
>>>>>>> Stashed changes
    }

    public ElevatorButton getNearestButton(Vector2D data) {
        LinkedList<ElevatorButton> elevatorButtons = new LinkedList<>();
        floors.forEach(floor -> floor.getBorders().forEach(
                border -> elevatorButtons.add(border.getElevatorButton())));

        return elevatorButtons.stream()
                .reduce(null, (elevatorButtonA, elevatorButtonB) -> {
                    if (elevatorButtonA == null) {
                        return elevatorButtonB;
                    }
                    if (elevatorButtonB == null) {
                        return elevatorButtonA;
                    }
                    if (data.getNearest(elevatorButtonA.getPosition(), elevatorButtonB.getPosition())
                            .equals(elevatorButtonA.getPosition())) {
                        return elevatorButtonA;
                    }
                    return elevatorButtonB;
                });
    }

<<<<<<< Updated upstream
=======
    public DrawableCustomer getCustomer(long id) {
        var ref = new Object() {
            DrawableCustomer customer = null;
        };
        streamOf(DrawableCustomer.class).filter(elevator -> elevator.getId() == id)
                .findFirst().ifPresentOrElse(customer -> ref.customer = customer, () -> {
                    throw new RuntimeException("Customer not fond.");
                });
        return ref.customer;
    }

>>>>>>> Stashed changes
    public DrawableElevator getElevator(long id) {
        var ref = new Object() {
            DrawableElevator foundDrawableElevator;
        };
        streamOf(DrawableElevator.class).filter(elevator -> elevator.getId() == id)
                .findFirst().ifPresentOrElse(drawableElevator -> ref.foundDrawableElevator = drawableElevator, () -> {
                    throw new RuntimeException("Elevator not found");
                });
        return ref.foundDrawableElevator;
    }

<<<<<<< Updated upstream
    public void setSettings(MainInitializationSettings settings) {
        this.settings = settings;
    }

    public boolean isNeedToInitialise() {
        return needToInitialize;
    }

    public void clear() {
        needToInitialize = true;
    }

    public void changeBehindElevatorForCustomer(long id) {
        customers.stream().filter(drawableCustomer -> drawableCustomer.getId() == id).findFirst().ifPresent(
                DrawableCustomer::changeBehindElevator);
    }


    private void applyArrivedData(List<Creature> creatures_came,
                                  LinkedList<? extends Creature> creatures_to_apply) {
        // erease
        creatures_to_apply.removeIf(
                creatureA -> creatures_came.stream().noneMatch(
                        creatureB -> creatureA.getId() == creatureB.getId()));
        // update
        creatures_to_apply.forEach(
                creatureA -> {
                    creatureA.set(creatures_came.stream().filter(
                            creatureB -> creatureA.getId() == creatureB.getId()
                    ).findFirst().get());
                }
        );
    }

    public void updateData(CreaturesData data) {
        this.applyArrivedData(data.ELEVATORS, elevators);
        this.applyArrivedData(data.CUSTOMERS, customers);

        // Add
        data.ELEVATORS.forEach(
                creatureA -> {
                    if (elevators.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())) {
                        elevators.add(
                                new DrawableElevator(creatureA, settings.ELEVATOR_OPEN_CLOSE_TIME,
                                        COLOR_SETTINGS.ELEVATOR_BACKGROUND_COLOR, COLOR_SETTINGS.ELEVATOR_DOOR_COLOR,
                                        COLOR_SETTINGS.ELEVATOR_BORDER_COLOR));
                    }
                }
        );

        data.CUSTOMERS.forEach(
                creatureA -> {
                    if (customers.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())) {
                        customers.add(new DrawableCustomer(creatureA,
                                COLOR_SETTINGS.CUSTOMER_SKIN_COLOR));
                    }
                }
        );
        if (isNeedToInitialise()) {
            initialiseFirstData();
        }
=======
    public void setRemoteConfig(ConnectionEstalblishConfig connectionEstalblishConfig) {
        combienedDrawSettings.setConnectionEstalblishConfig(connectionEstalblishConfig);
    }

    public void applyArivedData(GameMapCompactData data) {
        PackageLoader.applyArivedData(data, this, combienedDrawSettings);
>>>>>>> Stashed changes
    }
}
