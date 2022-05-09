package drawable.abstracts;

import configs.tools.CombienedDrawDataBase;
import drawable.drawTool.DrawTool;
import model.objects.Creature;
import tools.Vector2D;
import view.graphics.GameGraphics;

public abstract class DrawableCreature extends Drawable {
    final protected Creature creature;

    public DrawableCreature(Creature creature, DrawTool tool, CombienedDrawDataBase settings) {
        super(tool, settings);
        this.creature = creature;
    }

    @Override
    public final void draw(Vector2D realDrawPosition, GameGraphics gameDrawer) {
        this.realDrawPosition = realDrawPosition; // TODO check if works
        if (!getIsVisible()) {
            return;
        }

        Vector2D positionOfTheCreature = getShiftDrawPosition(getPosition(), getSize(), getDrawCenter());
        getTool().draw(positionOfTheCreature, getSize(), gameDrawer);
    }

    public final Vector2D getPosition() {
        return creature.getPosition();
    }

    public final Vector2D getSize() {
        return creature.getSize();
    }

    public final boolean getIsVisible() {
        return creature.isVisible();
    }

    protected void setSize(Vector2D size) {
        creature.setSize(size);
    }

    public final boolean isDead() {
        return creature.isDead();
    }

    protected long getId() {
        return creature.getId();
    }

}

/*


    public void draw(Drawable drawable, Color objectColor) {
        draw(drawable, objectColor, objectColor, 0);
    }

    public void draw(Drawable drawable, Color objectColor, Color borderColor, int thickness) {
        if (!drawable.getIsVisible()) {
            return;
        }
        Vector2D positionOfTheCreature = getShiftDrawPosition(drawable);
        this.drawFilledRect(positionOfTheCreature, drawable.getSize(), objectColor, borderColor, thickness);
    }

    public void draw(Drawable drawable, Color borderColor, double thickness) {
        if (!drawable.getIsVisible()) {
            return;
        }
        Vector2D positionOfTheCreature = getShiftDrawPosition(drawable);
        this.drawOnlyBorderRect(positionOfTheCreature, drawable.getSize(), borderColor, thickness);
    }

    public void draw(DrawableCreatureWithTexture drawableCreatureWithTexture) {
        Vector2D positionOfTheCreature = getShiftDrawPosition(drawableCreatureWithTexture);
        drawImage(drawableCreatureWithTexture.getImage(), positionOfTheCreature, drawableCreatureWithTexture.getSize());
    }
 */