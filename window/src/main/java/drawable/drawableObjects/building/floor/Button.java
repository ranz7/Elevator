package drawable.drawableObjects.building.floor;

import model.GuiModel;
import model.objects.movingObject.Creature;
import drawable.Drawable;
import tools.Vector2D;
import view.drawTools.GameDrawer;
import tools.Timer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Small buttons near elevators.
 */
public class Button extends Creature implements Drawable {
    private final long BUTTON_ON_TIME = 500;
    private final Timer BUTTON_ON_TIMER = new Timer();
    private final Color COLOR_ON;
    private final Color COLOR_OFF;

    public Button(Vector2D position, GuiModel guiModel) {
        super(position, guiModel.DRAW_SETTINGS.ButtonSize);
        this.COLOR_ON = guiModel.COLOR_SETTINGS.BUTTON_ON_COLOR;
        this.COLOR_OFF = guiModel.COLOR_SETTINGS.BUTTON_OF_COLOR;
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 10;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (BUTTON_ON_TIMER.isReady()) {
            gameDrawer.setColor(COLOR_ON);
        } else {
            gameDrawer.setColor(COLOR_OFF);
        }
        gameDrawer.fillRect(this);
    }

    @Override
    public List<Drawable> getDrawables() {
        var drawables = new LinkedList<Drawable>();
        drawables.add(this);
        return drawables;
    }

    @Override
    public void tick(long deltaTime) {
        BUTTON_ON_TIMER.tick(deltaTime);
    }

    public void buttonClick() {
        BUTTON_ON_TIMER.restart(BUTTON_ON_TIME);
    }
}
