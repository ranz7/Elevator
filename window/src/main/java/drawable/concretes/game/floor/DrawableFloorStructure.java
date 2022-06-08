package drawable.concretes.game.floor;

import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableRemoteCreature;
import drawable.concretes.game.customer.DrawableCustomer;
import drawable.concretes.game.elevator.DrawableElevator;
import drawable.concretes.game.floor.decorations.FloorPainting;
import drawable.concretes.game.floor.elevatorSpace.ElevatorBorder;
import drawable.concretes.game.floor.elevatorSpace.ElevatorButton;
import lombok.Getter;
import lombok.Setter;
import model.DatabaseOf;
import model.Transport;
import model.Transportable;
import model.packageLoader.DrawableCreatureData;
import protocol.special.CreatureType;
import drawable.abstracts.DrawCenter;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

import java.util.LinkedList;
import java.util.List;

public class DrawableFloorStructure extends DrawableRemoteCreature implements Transport<Drawable>, Transportable<Drawable> {
    @Setter
    @Getter
    private Transport<Drawable> transport;
    @Getter
    private final DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this,
            FloorHidingCornerWall.class,
            DrawableFloorStructure.class,
            DrawableElevator.class,
            DrawableCustomer.class,
            ElevatorBorder.class,
            ElevatorButton.class,
            FloorPainting.class,
            FloorBackground.class,
            UnderElevatorHidingWall.class);

    DrawableFloorStructure floorUnderUs;

    public DrawableFloorStructure(DrawableCreatureData data, LocalDrawSetting settings) {
        super(data, new RectangleWithBorder(settings.florBetonColor(), 7), settings);
        add(new FloorBackground(getSize(), settings));
        add(new FloorHidingCornerWall(
                new Vector2D(-settings.customerWidth() , -2),
                new Vector2D(settings.customerWidth()  , getSize().y + 2),
                settings
        ));
        add(new FloorHidingCornerWall(
                new Vector2D(getSize().x, -2),
                new Vector2D(settings.customerWidth() , getSize().y + 2),
                settings
        ));
    }

    boolean hidingWallWasAdded = false;

    public void updateElevatorBorders(List<DrawableElevator> elevators) {
        // borders are deleted automatically
        if (floorUnderUs != null) {
            floorUnderUs.updateElevatorBorders(elevators);
        }
        var elevatorsWithoutBorder = new LinkedList<>(elevators);
        var bordersOnFloor = localDataBase.streamOfOnlyOwned(ElevatorBorder.class).toList();
        elevatorsWithoutBorder.removeIf(
                drawableElevator -> {
                    // if none of borders know about elevator - than we need to create a border
                    return bordersOnFloor.stream().anyMatch(
                            border -> border.getParentElevator() == drawableElevator
                    );
                }
        );
        elevatorsWithoutBorder.forEach(
                elevatorWithoutBorder -> {
                    add(new ElevatorBorder(elevatorWithoutBorder.getPosition().withY(0),
                            elevatorWithoutBorder.getSize(), elevatorWithoutBorder, getSettings()));
                }
        );
        if (!hidingWallWasAdded) {
            add(new UnderElevatorHidingWall(elevators.get(0).getSize(), getSize(), getSettings()));
            hidingWallWasAdded = true;
        }
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int getDrawPriority() {
        return 14;
    }

    public boolean isBottomFloor() {
        return !(transport instanceof DrawableFloorStructure);
    }

    public int getCurrentFloorNum() {
        if (isBottomFloor()) {
            return 0;
        }
        return ((DrawableFloorStructure) transport).getCurrentFloorNum() + 1;
    }

    @Override
    public void add(Drawable drawable) {
        if (drawable instanceof DrawableCreatureData) {
            if (((DrawableCreatureData) drawable).getCreatureType() == CreatureType.FLOOR) {
                drawable = new DrawableFloorStructure((DrawableCreatureData) drawable, getSettings());
                floorUnderUs = (DrawableFloorStructure) drawable;
            } else if (((DrawableCreatureData) drawable).getCreatureType() == CreatureType.ELEVATOR_BUTTON) {
                drawable = new ElevatorButton((DrawableCreatureData) drawable, getSettings());
            } else if (((DrawableCreatureData) drawable).getCreatureType() == CreatureType.ELEVATOR) {
                drawable = new DrawableElevator((DrawableCreatureData) drawable, 1000, getSettings());
            } else if (((DrawableCreatureData) drawable).getCreatureType() == CreatureType.CUSTOMER) {
                drawable = new DrawableCustomer((DrawableCreatureData) drawable, getSettings());
            } else if (((DrawableCreatureData) drawable).getCreatureType() == CreatureType.FLOOR_PAINTING) {
                drawable = new FloorPainting((DrawableCreatureData) drawable, getSettings(), drawable.getId());
            } else {
                throw new RuntimeException("uncached " + ((DrawableCreatureData) drawable).getCreatureType());
            }
        }
        localDataBase.addCreature(drawable);
    }
}
