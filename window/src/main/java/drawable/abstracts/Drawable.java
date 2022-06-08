package drawable.abstracts;

import controller.Tickable;
import drawable.drawTool.DrawTool;
import model.objects.CreatureInterface;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;
import model.planes.graphics.Painter;

public interface Drawable extends CreatureInterface, Tickable {

    default void draw(Vector2D realDrawPosition, Painter gameDrawer) {
        if (!isVisible()) {
            return;
        }

        Vector2D positionOfTheCreature = getDrawCenter().getShiftDrawPosition(getPosition(), getSize());
        setRealDrawPosition(positionOfTheCreature.add(realDrawPosition)); // TODO check if works
        getTool().draw(positionOfTheCreature.add(realDrawPosition), getSize(), gameDrawer);
    }

    DrawTool getTool();

    LocalDrawSetting getSettings();

    void setRealDrawPosition(Vector2D realDrawPosition);

    boolean isVisible();

    Vector2D getPosition();

    Vector2D getSize();

    DrawCenter getDrawCenter();

    int getDrawPriority();

    void setPosition(Vector2D position);

    void setSize(Vector2D position);
}
