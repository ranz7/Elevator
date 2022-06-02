package drawable.buttons;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import model.planes.graphics.Painter;
import tools.Vector2D;

public class ClickableButton extends DrawableCreature implements Hoverable {
    private final DrawableCreature parasite;

    public ClickableButton(DrawableCreature parasite) {
        super(parasite.getPosition(), parasite.getSize(),
                parasite.getTool(), parasite.getSettings());
        this.parasite = parasite;
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
        set(parasite);
        parasite.tick(deltaTime);
    }

    @Override
    public void draw(Vector2D realDrawPosition, Painter gameDrawer) {
        super.draw(realDrawPosition, gameDrawer);
        parasite.draw(realDrawPosition, gameDrawer);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return parasite.getDrawCenter();
    }

    @Override
    public int getDrawPrioritet() {
        return parasite.getDrawPrioritet();
    }

    public void mousePositionUpdate(Vector2D gamePosition) {
    }
}
