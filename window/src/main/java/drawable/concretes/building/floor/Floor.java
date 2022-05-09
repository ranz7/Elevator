package drawable.concretes.building.floor;

import architecture.tickable.Tickable;
import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.Drawable;
import drawable.abstracts.withShape.creatures.DrawableLocalCreature;
import drawable.abstracts.DrawCenter;
import drawable.concretes.building.floor.decorations.FloorPainting;
import drawable.concretes.building.floor.elevatorSpace.ElevatorBorder;
import drawable.concretes.elevator.DrawableElevator;
import lombok.Getter;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Floor extends DrawableLocalCreature implements Tickable {

    private final LinkedList<UnderElevatorHidingWall> underElevatorHidingWall = new LinkedList<>();
    @Getter
    private final LinkedList<ElevatorBorder> borders = new LinkedList<>();
    private final LinkedList<FloorPainting> floorPaintings = new LinkedList<>();
    private final FloorBackground floorBackground;

    private final FloorHidingCornerWall floorHidingCornerWallLeft;
    private final FloorHidingCornerWall floorHidingCornerWallRight;


    int currentFloor;

    public Floor(int currentFloor, CombienedDrawDataBase settings) {
        super(new Vector2D(0, currentFloor * settings.floorHeight()),
                new Vector2D(settings.buildingSize().x, settings.floorHeight()), settings);
        this.currentFloor = currentFloor;

        floorBackground = new FloorBackground(currentFloor, settings);
        underElevatorHidingWall.add(new UnderElevatorHidingWall(currentFloor, settings));

        var buildingSize = settings.buildingSize();
        int floorHeight = (int) settings.floorHeight();
        var customerSize = settings.customerSize();

        floorHidingCornerWallLeft = new FloorHidingCornerWall(
                new Vector2D(0 - customerSize.x * 4., -2),
                new Vector2D(customerSize.x * 4, floorHeight + 2),
                settings
        );
        floorHidingCornerWallRight = new FloorHidingCornerWall(
                new Vector2D(buildingSize.x, -2),
                new Vector2D(customerSize.x * 4, floorHeight + 2),
                settings
        );

        addSubDrawable(floorHidingCornerWallLeft);
        addSubDrawable(floorHidingCornerWallRight);
        addSubDrawable(floorBackground);
        floorPaintings.forEach(this::addSubDrawable);
        borders.forEach(this::addSubDrawable);
        underElevatorHidingWall.forEach(this::addSubDrawable);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 14;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.draw(this, settings.florBetonColor(), 7);
    }

    public void updateElevatorBorders(List<DrawableElevator> elevators) {
        borders.clear();
        var random = new Random(settings.picturesGeneratorSeed());
        for (int j = 0; j < settings.elevatorsCount(); j++) {
            var distanceBetweenFloors = settings.floorHeight() * currentFloor;
            var elevatorBorderPosition = new Vector2D(settings.distanceBetweenElevators() * (j + 1), distanceBetweenFloors);
            borders.add(new ElevatorBorder(elevatorBorderPosition, elevators.get(j), settings));

            if (j > 0) {
                var decorationPosition = borders.get(j).getPosition()
                        .getAdded(borders.get(j - 1).getPosition())
                        .getAddedY(borders.get(j - 1).getSize().y)
                        .getDivided(2);
                floorPaintings.add(new FloorPainting(decorationPosition, settings, random));
            }
        }
    }
}
