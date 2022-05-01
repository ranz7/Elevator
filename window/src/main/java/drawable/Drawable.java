package drawable;

import view.drawTools.GameDrawer;

public interface Drawable {
    void draw(GameDrawer gameDrawer);
    void tick(long deltaTime);
}
