package model;


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
    }

    public ElevatorButton getNearestButton(Vector2D data) {
        return streamOf(ElevatorButton.class)
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

    public void setRemoteConfig(ConnectionEstalblishConfig connectionEstalblishConfig) {
        combienedDrawSettings.setConnectionEstalblishConfig(connectionEstalblishConfig);
    }

    public void applyArivedData(GameMapCompactData data) {
        PackageLoader.applyArivedData(data, this, combienedDrawSettings);
    }
}
