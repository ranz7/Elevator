package drawable.drawableConcrete.text;

import architecture.tickable.Tickable;
import drawable.drawableAbstract.DrawableLocalMoving;
import lombok.Getter;
import lombok.Setter;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

import java.awt.*;

public class DrawableLocalText extends DrawableLocalMoving implements Tickable {
    @Getter
    @Setter
    private  String text;
    @Getter
    private final Color textColor;

    public DrawableLocalText(String text, Vector2D position_start, TextData data) {
        super(position_start, data.getFontSize(), data.getTrajectory(), null);
        this.text = text;
        this.textColor = data.getTextColor();
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 16;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.draw(this);
    }

    public Color getColor() {
        return textColor;
    }

    @Override
    public boolean isDead() {
        return isReachedDestination();
    }
}
