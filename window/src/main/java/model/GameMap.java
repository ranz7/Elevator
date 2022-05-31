package model;

import controller.Tickable;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableRemoteCreature;
import drawable.concretes.game.floor.DrawableFloorStructure;
import drawable.concretes.game.floor.elevatorSpace.ElevatorButton;
import drawable.concretes.game.customer.DrawableCustomer;
import drawable.concretes.game.elevator.DrawableElevator;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import lombok.Getter;
import model.packageLoader.PackageLoader;
import protocol.special.GameMapCompactData;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;
import model.planes.graphics.Painter;

import java.awt.*;
import java.util.Comparator;


public class GameMap extends DrawableRemoteCreature implements Tickable, Transport {
    @Getter
    private final DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this);

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

    public void add(Drawable drawable) {
        localDataBase.add(drawable);
    }

    @Override
    public void draw(Vector2D realDrawPosition, Painter gameDrawer) {
        super.draw(realDrawPosition, gameDrawer);
        var objectsAndRelativePositions = localDataBase.toAbsolutePositionAndObjects();
        objectsAndRelativePositions.sort(Comparator.comparingInt(drawableObjet -> drawableObjet.getSecond().getDrawPrioritet()));
        objectsAndRelativePositions.forEach(
                positionAndObject -> {
                    positionAndObject.getSecond().draw(positionAndObject.getFirst(), gameDrawer);
                });
    }

    public Vector2D getBuildingSize() {
        var randomFloor = localDataBase.streamOf(DrawableFloorStructure.class).findFirst().get();
        var numberOfFloors = (double) localDataBase.countOf(DrawableFloorStructure.class);
        return new Vector2D(randomFloor.getSize()).multiplyY(numberOfFloors);
    }
}
