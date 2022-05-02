package drawable.drawableObjects.building.floor;

import drawable.Drawable;
import lombok.RequiredArgsConstructor;
import model.WindowModel;
import tools.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class Floor implements Drawable {
    final WindowModel WINDOW_MODEL;

    @Override
    public Integer GetDrawPrioritet() {
        return 14;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        var floorHeight = WINDOW_MODEL.getSettings().BUILDING_SIZE.y / WINDOW_MODEL.getSettings().FLOORS_COUNT;
        for (int i = 0; i < WINDOW_MODEL.getSettings().FLOORS_COUNT; i++) {
            gameDrawer.setColor(WINDOW_MODEL.COLOR_SETTINGS.BETON_COLOR);
            gameDrawer.drawRect(
                    new Vector2D(WINDOW_MODEL.getSettings().BUILDING_SIZE.x / 2., i * floorHeight),
                    new Point((int) WINDOW_MODEL.getSettings().BUILDING_SIZE.x, (int) floorHeight), 7);

            gameDrawer.setColor(WINDOW_MODEL.COLOR_SETTINGS.BLACK_SPACE_COLOR);
            gameDrawer.fillRect(
                    new Vector2D(0 - WINDOW_MODEL.getSettings().CUSTOMER_SIZE.x * 4., i * floorHeight - 2),
                    new Point(WINDOW_MODEL.getSettings().CUSTOMER_SIZE.x * 4, (int) floorHeight)
            );
            gameDrawer.fillRect(
                    new Vector2D(
                            WINDOW_MODEL.getSettings().BUILDING_SIZE.x, i * floorHeight - 2),
                    new Point(WINDOW_MODEL.getSettings().CUSTOMER_SIZE.x * 4, (int) floorHeight)
            );
        }
    }

    @Override
    public void tick(long deltaTime) {

    }

    @Override
    public List<Drawable> getDrawables() {
        var drawables = new LinkedList<Drawable>();
        drawables.add(this);
        return drawables;
    }
}
