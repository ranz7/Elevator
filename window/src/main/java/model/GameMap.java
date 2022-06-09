package model;

import controller.Tickable;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableRemoteCreature;
import drawable.concretes.FlyingText;
import drawable.concretes.game.floor.DrawableFloorStructure;
import drawable.concretes.game.floor.elevatorSpace.ElevatorButton;
import drawable.concretes.game.customer.DrawableCustomer;
import drawable.concretes.game.elevator.DrawableElevator;
import drawable.concretes.menu.Portal;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import gates.Gates;
import lombok.Getter;
import lombok.Setter;
import model.objects.CreatureInterface;
import model.packageLoader.DrawableCreatureData;
import protocol.Protocol;
import protocol.special.CreatureType;
import settings.RoomRemoteSettings;
import settings.localDraw.LocalDrawSetting;
import tools.Pair;
import tools.Vector2D;
import view.buttons.GameButtonComponent;

import java.awt.*;
import java.io.Serializable;

public class GameMap extends DrawableRemoteCreature implements Tickable, Transport<Drawable> {
    @Getter
    private final DatabaseOf<Drawable> localDataBase =
            new DatabaseOf<>(this,
                    FlyingText.class,
                    DrawableFloorStructure.class,
                    GameButtonComponent.class);
    @Getter
    @Setter
    RoomRemoteSettings roomRemoteSettings;

    public void initializeButtons(Portal portal) {
        add(new GameButtonComponent(portal, getSettings()));
    }

    public void removeButtons() {
        localDataBase.streamOfOnlyOwned(GameButtonComponent.class).findFirst().get().destroy();
    }

    private Gates gates;
    private LocalDrawSetting copySettings;
    public GameMap(DrawableCreatureData data, LocalDrawSetting settings, RoomRemoteSettings roomRemoteSettings, Gates gates) {
        super(data, new RectangleWithBorder(new Color(133, 101, 101), 7), new LocalDrawSetting());
        this.roomRemoteSettings = roomRemoteSettings;
        this.gates = gates;
        this.copySettings = settings;
    }

    @Override
    public void tick(double deltaTime) {
        getLocalDataBase().tick(roomRemoteSettings.gameSpeed() * deltaTime);
        updateBuildingSize();
        var elevators = getLocalDataBase().streamOf(DrawableElevator.class).toList();
        getLocalDataBase().streamOf(DrawableFloorStructure.class).forEach(
                drawableFloorStructure ->
                        drawableFloorStructure.updateElevatorBorders(elevators)
        );
        getLocalDataBase().removeIf(CreatureInterface::isDead);
    }

    Vector2D buildingSize = new Vector2D(0, 0);

    private void updateBuildingSize() {
        var randomFloor = localDataBase.streamOf(DrawableFloorStructure.class).findFirst().get();
        var numberOfFloors = (double) localDataBase.countOf(DrawableFloorStructure.class);
        this.buildingSize = new Vector2D(randomFloor.getSize()).multiplyY(numberOfFloors);
    }


    public ElevatorButton getNearestButton(Vector2D data) {
        return (ElevatorButton) localDataBase.streamTrio().filter(
                        creature -> creature.getThird() instanceof ElevatorButton
                )
                .reduce(null, (elevatorButtonA, elevatorButtonB) -> {
                    if (elevatorButtonB == null) {
                        return elevatorButtonA;
                    }
                    if (data.getNearest(elevatorButtonA.getSecond().add(elevatorButtonA.getThird().getPosition()),
                                    elevatorButtonB.getSecond().add(elevatorButtonB.getThird().getPosition()))
                            .equals(elevatorButtonA.getSecond().add(elevatorButtonA.getThird().getPosition()))) {
                        return elevatorButtonA;
                    }
                    return elevatorButtonB;
                }).getThird();
    }

    public DrawableCustomer getCustomer(int id) {
        var ref = new Object() {
            DrawableCustomer customer = null;
        };
        localDataBase
                .streamOf(DrawableCustomer.class)
                .filter(elevator -> elevator.getId() == id)
                .findFirst()
                .ifPresentOrElse(customer -> ref.customer = customer, () -> {
                    throw new RuntimeException("Customer not fond.");
                });
        return ref.customer;
    }

    public DrawableElevator getElevator(int id) {
        var ref = new Object() {
            DrawableElevator foundDrawableElevator;
        };
        localDataBase
                .streamOf(DrawableElevator.class)
                .filter(elevator -> elevator.getId() == id)
                .findFirst().ifPresentOrElse(drawableElevator -> ref.foundDrawableElevator = drawableElevator, () -> {
                    throw new RuntimeException("Elevator not found");
                });
        return ref.foundDrawableElevator;
    }


    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int getDrawPriority() {
        return 0;
    }

    @Override

    public void add(Drawable drawable) {
        if (drawable instanceof DrawableCreatureData) {
            if (((DrawableCreatureData) drawable).getCreatureType() == CreatureType.FLOOR) {
                drawable = new DrawableFloorStructure((DrawableCreatureData) drawable, getSettings());
            }
        }
        localDataBase.addCreature(drawable);
    }

    public Vector2D getBuildingSize() {
        return buildingSize;
    }

    public ElevatorButton getButton(int data) {
        var ref = new Object() {
            ElevatorButton found;
        };
        localDataBase.streamOf(ElevatorButton.class)
                .filter(button -> button.getId() == data)
                .findFirst()
                .ifPresentOrElse(
                        drawableElevator -> ref.found = drawableElevator, () -> {
                            throw new RuntimeException("Button not found");
                        });
        return ref.found;

    }


    public void createCustomer(int floorId, boolean fromLeft) {
        gates.sendWithoutCheck(Protocol.CREATE_CUSTOMER, getRoomRemoteSettings().roomId(),
                new Pair<Integer, Boolean>(floorId, fromLeft));
    }

    public void changeGameSpeed(boolean slow) {
        if (slow) {
            gates.sendWithoutCheck(Protocol.CHANGE_GAME_SPEED, getRoomRemoteSettings().roomId(), 0.5);
        } else {
            gates.sendWithoutCheck(Protocol.CHANGE_GAME_SPEED, getRoomRemoteSettings().roomId(), 1.5);
        }
    }

    public void changeThisColor() {
        getSettings().colorRandom();
    }

    public void changeAllColor() {
        copySettings.colorRandom();
    }
}
