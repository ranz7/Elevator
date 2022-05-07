package drawable.drawableObjectsConcrete.building.floor;

import configs.CanvasSettings.MainSettings;
import drawable.drawableAbstract.Drawable;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

public class UnderElevatorHidingWall extends Drawable {
    public UnderElevatorHidingWall(int currentFloor, MainSettings setting) {
        super(setting);
        super(new Vector2D(setting.buildingSize().x / 2., setting.elevatorSize().y + setting.floorHeight() * currentFloor),
                new Vector2D(setting.buildingSize().x, (model.getWallHeight() - model.getMainInitializationSettings().ELEVATOR_SIZE.y)));
        this.backGroundColor = model.colorSettings.FLOOR_WALL;
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 8;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(backGroundColor);
        gameDrawer.draw(this);
    }

}



