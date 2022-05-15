package drawable.concretes.building.floor;

import databases.CombienedDrawDataBase;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import model.objects.Creature;
import tools.Vector2D;

public class Floor extends DrawableCreature {
    public Floor(int currentFloor, CombienedDrawDataBase settings) {
        super(new Creature(
                        new Vector2D(0, currentFloor * settings.floorHeight()),
                        new Vector2D(settings.buildingSize().x, settings.floorHeight())),
                new RectangleWithBorder(settings.florBetonColor(), 7),
                settings);

        var buildingSize = settings.buildingSize();
        int floorHeight = (int) settings.floorHeight();
        var customerSize = settings.customerSize();

        addSubDrawable(new FloorHidingCornerWall(
                new Vector2D(0 - customerSize.x * 4., -2),
                new Vector2D(customerSize.x * 4, floorHeight + 2),
                settings
        ));
        addSubDrawable(new FloorHidingCornerWall(
                new Vector2D(buildingSize.x, -2),
                new Vector2D(customerSize.x * 4, floorHeight + 2),
                settings
        ));
        addSubDrawable(new FloorBackground( settings));
        addSubDrawable(new UnderElevatorHidingWall(settings));
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int GetDrawPrioritet() {
        return 14;
    }
}
