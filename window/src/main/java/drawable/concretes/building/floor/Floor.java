package drawable.concretes.building.floor;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.concretes.building.floor.decorations.FloorPainting;
import drawable.concretes.building.floor.elevatorSpace.ElevatorBorder;
import drawable.concretes.elevator.DrawableCreatureElevator;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import lombok.Getter;
import model.objects.Creature;
import tools.Vector2D;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Floor extends DrawableCreature {

    @Getter
    private final LinkedList<ElevatorBorder> borders = new LinkedList<>();
    private final LinkedList<FloorPainting> floorPaintings = new LinkedList<>();

    int currentFloor;

    public Floor(int currentFloor, CombienedDrawDataBase settings) {
        super(new Creature(
                        new Vector2D(0, currentFloor * settings.floorHeight()),
                        new Vector2D(settings.buildingSize().x, settings.floorHeight())),
                new RectangleWithBorder(settings.florBetonColor(), 7),
                settings);
        this.currentFloor = currentFloor;

        var buildingSize = settings.buildingSize();
        int floorHeight = (int) settings.floorHeight();
        var customerSize = settings.customerSize();

        addSubDrawable(new FloorHidingCornerWall(
                new Vector2D(0 - customerSize.x * 4., -2),
                new Vector2D(customerSize.x * 4, floorHeight + 2),
                settings
        ));
        addSubDrawable(new FloorHidingCornerWall(
                new Vector2D(buildingSize.x, -2),
                new Vector2D(customerSize.x * 4, floorHeight + 2),
                settings
        ));
        addSubDrawable(new FloorBackground( settings));
        addSubDrawable(new UnderElevatorHidingWall(settings));

        floorPaintings.forEach(this::addSubDrawable);
        borders.forEach(this::addSubDrawable);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int GetDrawPrioritet() {
        return 14;
    }

    public void updateElevatorBorders(List<DrawableCreatureElevator> elevators) {
        borders.clear();
        for (int j = 0; j < dataBase.elevatorsCount(); j++) {
            var random = new Random(dataBase.picturesGeneratorSeed()*j);
            var elevatorBorderPosition = new Vector2D(dataBase.distanceBetweenElevators() * (j + 1), 0);
            borders.add(new ElevatorBorder(elevatorBorderPosition, elevators.get(j), dataBase));

            if (j > 0) {
                var decorationPosition = borders.get(j).getPosition()
                        .getAdded(borders.get(j - 1).getPosition())
                        .getAddedY(borders.get(j - 1).getSize().y)
                        .getDivided(2);
                floorPaintings.add(new FloorPainting(decorationPosition, dataBase, random));
            }
        }
    }
}
