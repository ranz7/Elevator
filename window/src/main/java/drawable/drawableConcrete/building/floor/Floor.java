package drawable.drawableConcrete.building.floor;

import architecture.tickable.Tickable;
import configs.tools.CombienedDrawDataBase;
import drawable.drawableAbstract.Drawable;
import drawable.drawableAbstract.DrawableLocalCreature;
import drawable.drawableConcrete.building.floor.decorations.FloorPainting;
import drawable.drawableConcrete.building.floor.elevator.ElevatorBorder;
import drawable.drawableConcrete.elevator.DrawableElevator;
import lombok.Getter;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

//TODO CHANGE TO DrawableCreature
public class Floor extends DrawableLocalCreature implements Tickable {

    private final LinkedList<UnderElevatorHidingWall> underElevatorHidingWall = new LinkedList<>();
    @Getter
    private final LinkedList<ElevatorBorder> borders = new LinkedList<>();
    private final LinkedList<FloorPainting> floorPaintings = new LinkedList<>();
    private final FloorWall floorWall;
    int currentFloor;

    public Floor(int currentFloor, CombienedDrawDataBase settings) {
        super(new Vector2D(settings.buildingSize().x / 2, currentFloor * settings.floorHeight()),
                new Vector2D(settings.buildingSize().x, settings.floorHeight()), settings);
        this.currentFloor = currentFloor;

        floorWall = new FloorWall(currentFloor, settings);
        underElevatorHidingWall.add(new UnderElevatorHidingWall(currentFloor, settings));
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 14;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        var buildingSize = settings.buildingSize();
        int floorHeight = (int) settings.floorHeight();
        var customerSize = settings.customerSize();
        gameDrawer.draw(this, settings.florBetonColor(), 7);
        // gameDrawer.setColor(settings.windowBackGroundColor());
        //  gameDrawer.fillRect(
        //        new Vector2D(0 - customerSize.x * 4., currentFloor * floorHeight - 2),
        //         new Vector2D(customerSize.x * 4, floorHeight)
        // );
        // gameDrawer.fillRect(
        //        new Vector2D(buildingSize.x, currentFloor * floorHeight - 2),
        //         new Vector2D(customerSize.x * 4, floorHeight)
        // );

    }

    @Override
    public List<Drawable> getDrawables() {
        var drawables = super.getDrawables();
        // drawables.addAll(floorWall.getDrawables());
        // floorPaintings.forEach(drawable -> drawables.addAll(drawable.getDrawables()));
        // borders.forEach(drawable -> drawables.addAll(drawable.getDrawables()));
        // underElevatorHidingWall.forEach(drawable -> drawables.addAll(drawable.getDrawables()));
        return drawables;
    }

    public void updateElevatorBorders(List<DrawableElevator> elevators) {
        borders.clear();
        var random = new Random(settings.picturesGeneratorSeed());
        for (int j = 0; j < settings.elevatorsCount(); j++) {
            var distanceBetweenElevators = settings.distanceBetweenElevators() * (j + 1);
            var distanceBetweenFloors = settings.floorHeight() * currentFloor;
            var elevatorBorderPosition = new Vector2D(distanceBetweenElevators, distanceBetweenFloors);
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
