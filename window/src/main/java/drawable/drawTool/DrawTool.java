package drawable.drawTool;

import model.objects.Creature;
import view.drawTools.drawer.GameDrawer;

public abstract class DrawTool {
    public abstract void draw(GameDrawer drawer, Creature creature);
}
