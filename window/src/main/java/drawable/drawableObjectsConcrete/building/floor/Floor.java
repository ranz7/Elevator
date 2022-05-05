package drawable.drawableObjectsConcrete.building.floor;

import drawable.drawableBase.creatureWithTexture.Drawable;
import drawable.drawableObjectsConcrete.building.floor.decorations.FloorPainting;
import drawable.drawableObjectsConcrete.building.floor.elevator.ElevatorBorder;
import lombok.Getter;
import model.GuiModel;
import tools.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

//TODO CHANGE TO DrawableCreature
public class Floor implements Drawable {
    final GuiModel WINDOW_MODEL;

    private final LinkedList<UnderElevatorHidingWall> underElevatorHidingWall = new LinkedList<>();
    @Getter
    private final LinkedList<ElevatorBorder> borders = new LinkedList<>();
    private final LinkedList<FloorPainting> floorPaintings = new LinkedList<>();
    private final FloorWall floorWall;
    int currentFloor;

    public Floor(int currentFloor, GuiModel guiModel) {
        this.currentFloor = currentFloor;
        WINDOW_MODEL = guiModel;

        floorWall = new FloorWall(currentFloor, guiModel);
        underElevatorHidingWall.add(new UnderElevatorHidingWall(currentFloor, guiModel));

        for (int j = 0; j < guiModel.getSettings().ELEVATORS_COUNT; j++) {
            var elevatorBorderPosition = new Vector2D(
                    guiModel.getDistanceBetweenElevators() * (j + 1),
                    currentFloor * guiModel.getWallHeight());
            borders.add(new ElevatorBorder(elevatorBorderPosition, guiModel.getElevators().get(j), guiModel));

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
        var floorHeight = WINDOW_MODEL.getSettings().BUILDING_SIZE.y / WINDOW_MODEL.getSettings().FLOORS_COUNT;
        gameDrawer.setColor(WINDOW_MODEL.COLOR_SETTINGS.FLOOR_BETON_COLOR);
        gameDrawer.drawRect(
                new Vector2D(WINDOW_MODEL.getSettings().BUILDING_SIZE.x / 2., currentFloor * floorHeight),
                new Vector2D((int) WINDOW_MODEL.getSettings().BUILDING_SIZE.x, (int) floorHeight), 7);

        gameDrawer.setColor(WINDOW_MODEL.COLOR_SETTINGS.GUI_BACK_GROUND_COLOR);
        gameDrawer.drawFilledRect(
                new Vector2D(0 - WINDOW_MODEL.getSettings().CUSTOMER_SIZE.x * 4., currentFloor * floorHeight - 2),
                new Vector2D(WINDOW_MODEL.getSettings().CUSTOMER_SIZE.x * 4, (int) floorHeight)
        );
        gameDrawer.drawFilledRect(
                new Vector2D(
                        WINDOW_MODEL.getSettings().BUILDING_SIZE.x, currentFloor * floorHeight - 2),
                new Vector2D(WINDOW_MODEL.getSettings().CUSTOMER_SIZE.x * 4, (int) floorHeight)
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
