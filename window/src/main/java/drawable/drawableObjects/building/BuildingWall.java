package drawable.drawableObjects.building;

import drawable.Drawable;
import lombok.RequiredArgsConstructor;
import model.GuiModel;
import tools.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class BuildingWall implements Drawable {
    final Integer i;
    final GuiModel VIEW_MODEL;

    @Override
    public Integer GetDrawPrioritet() {
        return 0;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(VIEW_MODEL.COLOR_SETTINGS.WALL_COLOR);
        gameDrawer.fillRect(
                new Vector2D(0, i * VIEW_MODEL.getWallHeight()),
                new Point((int) VIEW_MODEL.getSettings().BUILDING_SIZE.x, (int) (double) VIEW_MODEL.getWallHeight()));
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
