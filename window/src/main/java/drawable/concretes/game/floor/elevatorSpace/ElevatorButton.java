package drawable.concretes.game.floor.elevatorSpace;

import drawable.abstracts.DrawableRemoteCreature;
import model.packageLoader.DrawableCreatureData;
import settings.RoomRemoteSettings;
import drawable.abstracts.DrawCenter;
import drawable.drawTool.figuresComponent.Rectangle;
import settings.localDraw.LocalDrawSetting;
import tools.Timer;
import tools.Vector2D;

/**
 * Small buttons near elevators.
 */
public class ElevatorButton extends DrawableRemoteCreature {
    private final Timer buttonOnTimer = new Timer();

    public ElevatorButton(DrawableCreatureData creatureData, LocalDrawSetting settings) {
        super(creatureData,new Rectangle(settings.buttonColorOff()), settings);
    }

    @Override
    public void tick(double deltaTime) {
        buttonOnTimer.tick(deltaTime);
        if (buttonOnTimer.isReady()) {
            getTool().setColor(getSettings().buttonColorOn().getColor());
        } else {
            getTool().setColor(getSettings().buttonColorOff().getColor());
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
    public int getDrawPriority() {
        return 10;
    }
}
