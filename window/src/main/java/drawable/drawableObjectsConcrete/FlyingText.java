package drawable.drawableObjectsConcrete;

import drawable.drawableBase.creatureWithTexture.Drawable;
import model.objects.movingObject.MovingObject;
import tools.tools.Vector2D;
import view.drawTools.GameDrawer;
import tools.tools.Timer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class FlyingText extends MovingObject implements Drawable {
    private final int FONT_SIZE;
    private final String TEXT;
    private final Timer TIMER;
    private final Color TEXT_COLOR;

    public FlyingText(String TEXT, Vector2D position_start, Vector2D vector_to_fly,
                      int font_size, double speed, long life_time, Color textColor) {
        super(position_start, speed);
        this.FONT_SIZE = font_size;
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

    @Override
    public List<Drawable> getDrawables() {
        var drawables = new LinkedList<Drawable>();
        drawables.add(this);
        return drawables;
    }
}
