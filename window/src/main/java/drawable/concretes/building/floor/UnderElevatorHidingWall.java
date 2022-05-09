package drawable.concretes.building.floor;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.withShape.creatures.DrawableLocalCreature;
import drawable.abstracts.DrawCenter;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

public class UnderElevatorHidingWall extends DrawableLocalCreature {
    public UnderElevatorHidingWall(int currentFloor, CombienedDrawDataBase setting) {
        super(new Vector2D(0,
                        setting.elevatorSize().y + setting.floorHeight() * currentFloor),
                new Vector2D(setting.buildingSize().x,
                        (setting.floorHeight() - setting.elevatorSize().y)),
                setting);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 8;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.draw(this, settings.floorWallColor());
    }

}



