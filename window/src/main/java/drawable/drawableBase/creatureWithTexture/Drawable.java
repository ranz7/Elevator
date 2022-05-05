package drawable.drawableBase.creatureWithTexture;

import architecture.tickable.Tickable;
import view.drawTools.GameDrawer;

import java.util.List;

public interface Drawable extends Tickable {
    void draw(GameDrawer gameDrawer);

    List<Drawable> getDrawables();

    Integer GetDrawPrioritet();
}
