package drawable.drawableBase.creatureWithTexture;

import architecture.tickable.Tickable;
import view.drawTools.GameDrawer;

import java.util.List;

public abstract interface Drawable extends Tickable {
    abstract void draw(GameDrawer gameDrawer);

    abstract List<Drawable> getDrawables();

    abstract Integer GetDrawPrioritet();
}
