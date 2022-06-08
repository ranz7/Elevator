package drawable.buttons;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableCreature;
import lombok.Getter;
import lombok.Setter;
import model.planes.graphics.Painter;
import tools.Vector2D;

import java.awt.*;

public class ClickableButton extends DrawableCreature implements Hoverable {
    @Getter
    private final Drawable parasite;
    @Getter
    @Setter
    private boolean hovered = false;
    private Runnable toExecute;

    public ClickableButton(Drawable parasite, Runnable toExecute) {
        super(parasite.getPosition(), parasite.getSize().multiply(1.2),
                parasite.getTool(), parasite.getSettings());
        this.parasite = parasite;
        this.toExecute = toExecute;
        set(parasite);
    }

    @Override
    public void tick(double deltaTime) {
        parasite.tick(deltaTime);
        setPosition(getParasite().getPosition());
    }

    @Override
    public void draw(Vector2D realDrawPosition, Painter gameDrawer) {
        if (hovered) {
            setSize(getParasite().getSize().multiply(1.3));
        } else {
            setSize(getParasite().getSize());
        }
        super.draw(realDrawPosition, gameDrawer);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return getParasite().getDrawCenter();
    }

    @Override
    public int getDrawPriority() {
        return getParasite().getDrawPriority();
    }

    public void mousePositionUpdate(Vector2D gamePosition) {
        if (!isVisible()) {
            hovered = false;
            return;
        }
        if (getTool().isIntersect(getRealDrawPosition(), getSize(), gamePosition)) {
            getTool().setAdditionalLightColor(new Color(89, 20, 20));
            hovered = (true);
        } else {
            getTool().setAdditionalLightColor(new Color(0, 0, 0));
            hovered = (false);
        }
    }

    public void execute() {
        toExecute.run();
    }

}
