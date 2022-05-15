package drawable.abstracts;

import drawable.drawTool.DrawTool;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;
import view.graphics.GameGraphics;

public interface Drawable {

    default void draw(Vector2D realDrawPosition, GameGraphics gameDrawer) {
        if (!isVisible()) {
            return;
        }

        Vector2D positionOfTheCreature = getDrawCenter().getShiftDrawPosition(getPosition(), getSize());
        setRealDrawPosition(positionOfTheCreature); // TODO check if works
        getTool().draw(positionOfTheCreature.getAdded(realDrawPosition), getSize(), gameDrawer);
    }

    DrawTool getTool();

    LocalDrawSetting getSettings();

    boolean isVisible();

    Vector2D getPosition();

    Vector2D getSize();

    void setRealDrawPosition(Vector2D realDrawPosition);

    DrawCenter getDrawCenter();

    int GetDrawPrioritet();
}
