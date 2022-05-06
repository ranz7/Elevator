package drawable.drawableObjectsConcrete;

import architecture.tickable.Tickable;
import configs.CanvasSettings.MainSettings;
import drawable.drawableBase.DrawableMoving;
import model.objects.movingObject.MovingObject;
import tools.Vector2D;
import view.drawTools.GameDrawer;
import tools.Timer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class FlyingText extends DrawableMoving implements Tickable {
    private final String TEXT;
    private final Timer TIMER;
    private final Color TEXT_COLOR;

    public FlyingText(String TEXT, Vector2D position_start, Vector2D vector_to_fly,
                      int font_size, double speed, long life_time, Color textColor, MainSettings settings) {
        super(position_start, font_size,speed, settings);
        this.destination = position_start.getAdded(vector_to_fly.getMultiplied(life_time * speed));
        this.TEXT = TEXT;
        TIMER = new Timer();
        TIMER.restart(life_time);
        this.TEXT_COLOR = textColor;
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 16;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.setColor(TEXT_COLOR);

        gameDrawer.setFont("TimesRoman", Font.PLAIN, FONT_SIZE);
        gameDrawer.draw(TEXT, position);
    }

    @Override
    public void tick(long delta_time) {
        super.tick(delta_time);
        TIMER.tick(delta_time);
        if (TIMER.isReady()) {
            isDead = true;
        }
    }
}
