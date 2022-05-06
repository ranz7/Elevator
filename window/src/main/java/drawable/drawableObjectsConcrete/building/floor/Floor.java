package drawable.drawableObjectsConcrete.building.floor;

import architecture.tickable.Tickable;
import configs.CanvasSettings.MainSettings;
import drawable.drawableBase.Drawable;
import drawable.drawableBase.DrawableCreature;
import drawable.drawableObjectsConcrete.building.floor.decorations.FloorPainting;
import drawable.drawableObjectsConcrete.building.floor.elevator.ElevatorBorder;
import drawable.drawableObjectsConcrete.elevator.DrawableElevator;
import lombok.Getter;
import tools.Vector2D;
import view.drawTools.GameDrawer;

import java.util.LinkedList;
import java.util.List;

//TODO CHANGE TO DrawableCreature
public class Floor extends DrawableCreature implements Tickable {

    private final LinkedList<UnderElevatorHidingWall> underElevatorHidingWall = new LinkedList<>();
    @Getter
    private final LinkedList<ElevatorBorder> borders = new LinkedList<>();
    private final LinkedList<FloorPainting> floorPaintings = new LinkedList<>();
    private final FloorWall floorWall;
    int currentFloor;

    public Floor(int currentFloor, List<DrawableElevator> elevators, MainSettings settings) {
        super(new Vector2D(settings.buildingSize().x, currentFloor * settings.floorHeight()),
                new Vector2D(buildingSize.x, floorHeight), settings);
        this.currentFloor = currentFloor;

        floorWall = new FloorWall(currentFloor, settings);
        underElevatorHidingWall.add(new UnderElevatorHidingWall(currentFloor, settings));

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
                floorPaintings.add(new FloorPainting(decorationPosition));
            }
        }
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 14;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        var buildingSize = WINDOW_MODEL.getMainSettings().buildingSize();
        int floorHeight = (int) WINDOW_MODEL.getMainSettings().floorHeight();
        var customerSize = WINDOW_MODEL.getMainSettings().customerSize();
        gameDrawer.setColor(WINDOW_MODEL.getMainSettings().betonCollor());
        gameDrawer.drawRect(
                new Vector2D(buildingSize.x / 2., currentFloor * floorHeight),
                new Vector2D(buildingSize.x, floorHeight), 7);

        gameDrawer.setColor(WINDOW_MODEL.getMainSettings().windowBackGround());
        gameDrawer.drawFilledRect(
                new Vector2D(0 - customerSize.x * 4., currentFloor * floorHeight - 2),
                new Vector2D(customerSize.x * 4, floorHeight)
        );
        gameDrawer.drawFilledRect(
                new Vector2D(buildingSize.x, currentFloor * floorHeight - 2), new Vector2D(customerSize.x * 4, floorHeight)
        );

    }

    @Override
    public void tick(long deltaTime) {

    }

    @Override
    public List<Drawable> getDrawables() {
        var drawables = new LinkedList<Drawable>();
        drawables.add(this);
        drawables.addAll(floorWall.getDrawables());

        floorPaintings.forEach(drawable -> drawables.addAll(drawable.getDrawables()));
        borders.forEach(drawable -> drawables.addAll(drawable.getDrawables()));
        underElevatorHidingWall.forEach(drawable -> drawables.addAll(drawable.getDrawables()));
        return drawables;
    }
}
