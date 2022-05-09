package drawable.concretes.building.floor.elevatorSpace;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.withShape.creatures.DrawableLocalCreature;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;
import tools.Timer;

import java.awt.*;

/**
 * Small buttons near elevators.
 */
public class ElevatorButton extends DrawableLocalCreature {
    private final Timer BUTTON_ON_TIMER = new Timer();

    public ElevatorButton(Vector2D position, CombienedDrawDataBase settings) {
        super(position, settings.elevatorButtonSize(), settings);
    }

    @Override
    public void tick(double deltaTime) {
        BUTTON_ON_TIMER.tick(deltaTime);
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        Color drawColor = BUTTON_ON_TIMER.isReady() ? settings.buttonColorOn() : settings.buttonColorOff();
        gameDrawer.draw(this, drawColor);
    }

    public void buttonClick() {
        BUTTON_ON_TIMER.restart(settings.buttonOnTime());
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 10;
    }
}
