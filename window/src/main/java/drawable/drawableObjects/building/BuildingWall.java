package drawable.drawableObjects.building;

import drawable.Drawable;
import lombok.RequiredArgsConstructor;
import model.WindowModel;
import model.objects.movingObject.Creature;
import tools.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;

@RequiredArgsConstructor
public class BuildingWall implements Drawable {
    final WindowModel VIEW_MODEL;

    @Override
    public void draw(GameDrawer gameDrawer) {
        var floorHeight = VIEW_MODEL.getSettings().BUILDING_SIZE.y / VIEW_MODEL.getSettings().FLOORS_COUNT;
        for (int i = 0; i < VIEW_MODEL.getSettings().FLOORS_COUNT; i++) {
            gameDrawer.setColor(VIEW_MODEL.COLOR_SETTINGS.WALL_COLOR);
            gameDrawer.fillRect(
                    new Vector2D(0, i * floorHeight),
                    new Point((int) VIEW_MODEL.getSettings().BUILDING_SIZE.x, (int) floorHeight));
        }
    }

    @Override
    public void tick(long deltaTime) {
    }
}
