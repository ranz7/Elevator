package drawable;

import view.drawTools.GameDrawer;

import java.util.List;

public interface Drawable {
    Integer GetDrawPrioritet();

    void draw(GameDrawer gameDrawer);

    List<Drawable> getDrawables();

    void tick(long deltaTime);
}
