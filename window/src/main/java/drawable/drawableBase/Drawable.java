package drawable.drawableBase;

import architecture.tickable.Tickable;
import configs.CanvasSettings.MainSettings;
import drawable.drawableBase.drawableWithTexture.DrawCenter;
import lombok.RequiredArgsConstructor;
import tools.Vector2D;
import view.drawTools.GameDrawer;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public abstract class Drawable implements Tickable {
    final protected MainSettings settings;

    @Override
    public void tick(long deltaTime) {
    }
    public abstract void draw(GameDrawer gameDrawer);

    public abstract Integer GetDrawPrioritet();

    public drawable.drawableBase.drawableWithTexture.DrawCenter getDrawCenter() {
        return DrawCenter.CENTER_BY_X;
    }

    public List<Drawable> getDrawables() {
        var drawables = new LinkedList<Drawable>();
        drawables.add(this);
        return drawables;
    }

    public abstract Vector2D getPosition();

    public abstract Vector2D getSize();

    public abstract boolean getIsVisible();
}
