package drawable.drawableObjectsConcrete.building.floor.elevator;

import drawable.DrawSettings;
import drawable.drawableBase.creatureWithTexture.DrawableCreature;
import model.GuiModel;
import common.Vector2D;
import view.drawTools.GameDrawer;
import common.Timer;

import java.awt.*;

/**
 * Small buttons near elevators.
 */
public class ElevatorButton extends DrawableCreature {
    private final long BUTTON_ON_TIME;
    private final Timer BUTTON_ON_TIMER = new Timer();
    private final Color COLOR_ON;
    private final Color COLOR_OFF;

    public ElevatorButton(Vector2D position, GuiModel guiModel) {
        super(position, guiModel.DRAW_SETTINGS.ELEVATOR_BUTTON_SIZE);
        this.COLOR_ON = guiModel.COLOR_SETTINGS.ELEVATOR_BUTTON_ON_COLOR;
        this.COLOR_OFF = guiModel.COLOR_SETTINGS.ELEVATOR_BUTTON_OF_COLOR;
        this.BUTTON_ON_TIME = DrawSettings.ELEVATOR_BUTTON_ON_TIME;
    }

    @Override
    public void tick(long deltaTime) {
        BUTTON_ON_TIMER.tick(deltaTime);
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (BUTTON_ON_TIMER.isReady()) {
            gameDrawer.setColor(COLOR_ON);
        } else {
            gameDrawer.setColor(COLOR_OFF);
        }
        gameDrawer.drawFilledRect(this);
    }

    public void buttonClick() {
        BUTTON_ON_TIMER.restart(BUTTON_ON_TIME);
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 10;
    }
}
