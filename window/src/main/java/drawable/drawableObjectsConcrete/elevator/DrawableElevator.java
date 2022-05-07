package drawable.drawableObjectsConcrete.elevator;

import configs.tools.CombienedDrawDataBase;
import drawable.drawableAbstract.Drawable;
import drawable.drawableAbstract.DrawableRemoteCreature;
import model.objects.Creature;
import view.drawTools.drawer.GameDrawer;
import lombok.Getter;

import java.awt.*;
import java.util.List;

@Getter
public class DrawableElevator extends DrawableRemoteCreature {
    public final Color BACK_GROUND_COLOR;
    public final ElevatorDoors DOORS;

    public DrawableElevator(Creature creature, CombienedDrawDataBase settings) {
        super(creature, settings);
        BACK_GROUND_COLOR = settings.elevatorBackGroundColor();
        DOORS = new ElevatorDoors(this, settings);
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 4;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (DOORS.isClosed()) {
            return;
        }
        gameDrawer.setColor(BACK_GROUND_COLOR);
        gameDrawer.draw(this);
    }

    @Override
    public List<Drawable> getDrawables() {
        List<Drawable> drawables = super.getDrawables();
        drawables.add(DOORS);
        return drawables;
    }
}
