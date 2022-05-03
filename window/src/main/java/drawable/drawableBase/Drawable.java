package drawable.drawableBase;

import view.drawTools.GameDrawer;

import java.util.List;

public interface Drawable {
    void draw(GameDrawer gameDrawer);
    void tick(long deltaTime);

    List<Drawable> getDrawables();
    Integer GetDrawPrioritet();
}
