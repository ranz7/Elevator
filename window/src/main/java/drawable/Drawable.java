package drawable;

import view.GameDrawer;

public interface Drawable {
    void draw(GameDrawer gameDrawer);
    void tick(long deltaTime);
}
