package drawable.buttons;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import lombok.Getter;
import lombok.Setter;
import model.planes.graphics.Painter;
import tools.Vector2D;

import java.awt.*;

public class ClickableButton extends DrawableCreature implements Hoverable {
    private final DrawableCreature parasite;
    @Getter
    @Setter
    private boolean hovered = false;
    private Runnable toExecute;

    public ClickableButton(DrawableCreature parasite, Runnable toExecute) {
        super(parasite.getPosition(), parasite.getSize().multiply(1.7),
                parasite.getTool(), parasite.getSettings());
        this.parasite = parasite;
        this.toExecute = toExecute;
    }

    @Override
    public void tick(double deltaTime) {
        set(parasite);
        parasite.tick(deltaTime);
    }

    @Override
    public void draw(Vector2D realDrawPosition, Painter gameDrawer) {
        if (hovered) {
            setSize(parasite.getSize().multiply(1.3));
        }
        super.draw(realDrawPosition, gameDrawer);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return parasite.getDrawCenter();
    }

    @Override
    public int getDrawPriority() {
        return parasite.getDrawPriority()+1;
    }

    public void mousePositionUpdate(Vector2D gamePosition) {
        if (getTool().isIntersect(getRealDrawPosition(), getSize(), gamePosition)) {
            getTool().setAdditionalLightColor(new Color(189, 0, 0));
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
