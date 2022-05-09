package drawable.concretes.elevator;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.Drawable;
import drawable.abstracts.withShape.creatures.DrawableRemoteCreature;
import model.objects.Creature;
import view.drawTools.drawer.GameDrawer;
import lombok.Getter;

import java.awt.*;
import java.util.List;

@Getter
public class DrawableElevator extends DrawableRemoteCreature {
    public final Color BACK_GROUND_COLOR;
    public final ElevatorDoors doors;

    public DrawableElevator(Creature creature, CombienedDrawDataBase settings) {
        super(creature, settings);
        BACK_GROUND_COLOR = settings.elevatorBackGroundColor();
        doors = new ElevatorDoors(this, settings);
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 4;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (doors.isClosed()) {
            return;
        }
        gameDrawer.draw(this,BACK_GROUND_COLOR);
    }

    @Override
    public List<Drawable> getDrawables() {
        List<Drawable> drawables = super.getDrawables();
        drawables.addAll(doors.getDrawables());
        return drawables;
    }
}
