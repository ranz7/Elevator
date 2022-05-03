package drawable.drawableObjectsConcrete.building.floor;

import drawable.drawableBase.creatureWithTexture.Drawable;
import lombok.RequiredArgsConstructor;
import model.GuiModel;
import common.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

// TODO CHANGE TO DRAWABLE CREATURE
@RequiredArgsConstructor
public class FloorWall implements Drawable {
    private final Integer floorNumber;
    private final GuiModel VIEW_MODEL;

    @Override
    public Integer GetDrawPrioritet() {
        return 0;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(VIEW_MODEL.COLOR_SETTINGS.FLOOR_WALL_COLOR);
        gameDrawer.drawFilledRect(
                new Vector2D(0, floorNumber * VIEW_MODEL.getWallHeight()),
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