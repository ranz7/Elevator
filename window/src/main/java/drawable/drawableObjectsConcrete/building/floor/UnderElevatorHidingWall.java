package drawable.drawableObjectsConcrete.building.floor;

import configs.tools.CombienedDrawDataBase;
import drawable.drawableAbstract.DrawableLocalCreature;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

public class UnderElevatorHidingWall extends DrawableLocalCreature {
    public UnderElevatorHidingWall(int currentFloor, CombienedDrawDataBase setting) {
        super(
                new Vector2D(setting.buildingSize().x / 2.,
                        setting.elevatorSize().y + setting.floorHeight() * currentFloor),
                new Vector2D(setting.buildingSize().x,
                        (setting.floorHeight() - setting.elevatorSize().y)),
                setting);
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 8;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(settings.floorWallColor());
        gameDrawer.draw(this);
    }

}



