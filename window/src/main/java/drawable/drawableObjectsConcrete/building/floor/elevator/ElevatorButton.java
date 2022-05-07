package drawable.drawableObjectsConcrete.building.floor.elevator;

import configs.tools.CombienedDrawDataBase;
import drawable.drawableAbstract.DrawableLocalCreature;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;
import tools.Timer;

/**
 * Small buttons near elevators.
 */
public class ElevatorButton extends DrawableLocalCreature {
    private final Timer BUTTON_ON_TIMER = new Timer();

    public ElevatorButton(Vector2D position, CombienedDrawDataBase settings) {
        super(position, settings.elevatorButtonSize(), settings);
    }

    @Override
    public void tick(long deltaTime) {
        BUTTON_ON_TIMER.tick(deltaTime);
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (BUTTON_ON_TIMER.isReady()) {
            gameDrawer.setColor(settings.buttonColorOn());
        } else {
            gameDrawer.setColor(settings.buttonColorOff());
        }
        gameDrawer.draw(this);
    }

    public void buttonClick() {
        BUTTON_ON_TIMER.restart(settings.buttonOnTime());
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 10;
    }
}
