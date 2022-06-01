package model;

import controller.Tickable;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableCreature;
import drawable.abstracts.DrawableRemoteCreature;
import drawable.concretes.FlyingText;
import drawable.concretes.game.floor.DrawableFloorStructure;
import drawable.concretes.game.floor.elevatorSpace.ElevatorButton;
import drawable.concretes.game.customer.DrawableCustomer;
import drawable.concretes.game.elevator.DrawableElevator;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import drawable.drawTool.text.Text;
import lombok.Getter;
import model.packageLoader.PackageLoader;
import protocol.special.GameMapCompactData;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;
import model.planes.graphics.Painter;

import java.awt.*;
import java.util.Comparator;


public class GameMap extends DrawableRemoteCreature implements Tickable, Transport<Drawable> {
    @Getter
    private final DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this, FlyingText.class);

    public GameMap(LocalDrawSetting settings) {
        super(new RectangleWithBorder(new Color(222, 222, 222), 7), settings);
    }

    @Override
    public void tick(double deltaTime) {
        localDataBase.tick(deltaTime);
        localDataBase.removeIf(Drawable::isDead);
    }

    public ElevatorButton getNearestButton(Vector2D data) {
        return localDataBase.streamOf(ElevatorButton.class)
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

    public DrawableCustomer getCustomer(int id) {
        var ref = new Object() {
            DrawableCustomer customer = null;
        };
        localDataBase.streamOf(DrawableCustomer.class).filter(elevator -> elevator.getId() == id)
                .findFirst().ifPresentOrElse(customer -> ref.customer = customer, () -> {
                    throw new RuntimeException("Customer not fond.");
                });
        return ref.customer;
    }

    public DrawableElevator getElevator(int id) {
        var ref = new Object() {
            DrawableElevator foundDrawableElevator;
        };
        localDataBase.streamOf(DrawableElevator.class).filter(elevator -> elevator.getId() == id)
                .findFirst().ifPresentOrElse(drawableElevator -> ref.foundDrawableElevator = drawableElevator, () -> {
                    throw new RuntimeException("Elevator not found");
                });
        return ref.foundDrawableElevator;
    }


    public void applyArivedData(GameMapCompactData data) {
        PackageLoader.applyArivedData(data, getLocalDataBase());
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.center;
    }

    @Override
    public int getDrawPrioritet() {
        return 0;
    }

    @Override
    public void add(Drawable drawable) {
        localDataBase.addCreature(drawable);
    }

    public Vector2D getBuildingSize() {
        var randomFloor = localDataBase.streamOf(DrawableFloorStructure.class).findFirst().get();
        var numberOfFloors = (double) localDataBase.countOf(DrawableFloorStructure.class);
        return new Vector2D(randomFloor.getSize()).multiplyY(numberOfFloors);
    }
}
