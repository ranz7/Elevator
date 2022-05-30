package drawable.concretes.game.floor.elevatorSpace;

import drawable.abstracts.DrawableRemoteCreature;
import settings.CombienedDrawSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import model.objects.Creature;
import tools.Timer;
import tools.Vector2D;

/**
 * Small buttons near elevators.
 */
public class ElevatorButton extends DrawableRemoteCreature {
    private final Timer buttonOnTimer = new Timer();

    public ElevatorButton(Vector2D position, CombienedDrawSettings settings) {
        super(new Rectangle(settings.buttonColorOff()), settings);
    }

    @Override
    public void tick(double deltaTime) {
        buttonOnTimer.tick(deltaTime);
        if (buttonOnTimer.isReady()) {
            getTool().setColor(getSettings().buttonColorOn());
        } else {
            getTool().setColor(getSettings().buttonColorOff());
        }
    }

    public void buttonClick() {
        buttonOnTimer.restart(getSettings().buttonOnTime());
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int GetDrawPrioritet() {
        return 10;
    }
}
