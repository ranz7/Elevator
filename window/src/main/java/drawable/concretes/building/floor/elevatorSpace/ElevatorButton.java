package drawable.concretes.building.floor.elevatorSpace;

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
public class ElevatorButton extends DrawableCreature {
    private final Timer buttonOnTimer = new Timer();

    public ElevatorButton(Vector2D position, CombienedDrawSettings settings) {
        super(new Creature(position, settings.elevatorButtonSize()), new Rectangle(settings.buttonColorOff()), settings);
    }

    @Override
    public void tick(double deltaTime) {
        buttonOnTimer.tick(deltaTime);
        if (buttonOnTimer.isReady()) {
            getTool().setColor(dataBase.buttonColorOn());
        } else {
            getTool().setColor(dataBase.buttonColorOff());
        }
    }

    public void buttonClick() {
        buttonOnTimer.restart(dataBase.buttonOnTime());
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
