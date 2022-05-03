package drawable.drawableObjectsConcrete.building.floor;

import drawable.drawableBase.creatureWithTexture.DrawableCreature;
import model.GuiModel;
import common.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;

public class UnderElevatorHidingWall extends DrawableCreature {
    private final Color BACK_GROUND_COLOR;

    public UnderElevatorHidingWall(int i, GuiModel model) {
        super(new Vector2D(model.getSettings().BUILDING_SIZE.x / 2., model.getSettings().ELEVATOR_SIZE.y + model.getWallHeight() * i),
                new Point((int) model.getSettings().BUILDING_SIZE.x, (int) (model.getWallHeight() - model.getSettings().ELEVATOR_SIZE.y)));
        this.BACK_GROUND_COLOR = model.COLOR_SETTINGS.FLOOR_WALL_COLOR;
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 8;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(BACK_GROUND_COLOR);
        gameDrawer.drawFilledRect(this);
    }

}



