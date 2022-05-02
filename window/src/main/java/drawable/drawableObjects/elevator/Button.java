package drawable.drawableObjects.elevator;

import model.objects.movingObject.Creature;
import drawable.Drawable;
import tools.Vector2D;
import view.drawTools.GameDrawer;
import tools.Timer;

import java.awt.*;

/**
 * Small buttons near elevators.
 */
public class Button extends Creature implements Drawable {
    private final long BUTTON_ON_TIME = 500;
    private final Timer BUTTON_ON_TIMER = new Timer();
    private final Color COLOR_ON;
    private final Color COLOR_OFF;

    public Button(Vector2D position, Point size, Color colorOn, Color colorOff) {
        super(position, size);
        this.COLOR_ON = colorOn;
        this.COLOR_OFF = colorOff;
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
    public void tick(long deltaTime) {
        BUTTON_ON_TIMER.tick(deltaTime);
    }

    public void buttonClick() {
        BUTTON_ON_TIMER.restart(BUTTON_ON_TIME);
    }
}
