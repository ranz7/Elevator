package drawable.drawableAbstract;

import architecture.tickable.Tickable;
import configs.tools.CombienedDrawDataBase;
import drawable.drawableAbstract.drawableWithTexture.DrawCenter;
import lombok.RequiredArgsConstructor;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public abstract class Drawable implements Tickable {
    final protected CombienedDrawDataBase settings;

    @Override
    public void tick(double deltaTime) {
    }

    public DrawCenter getDrawCenter() {
        return DrawCenter.MIDDLE_BY_X;
    }

    public List<Drawable> getDrawables() {
        var drawables = new LinkedList<Drawable>();
        drawables.add(this);
        return drawables;
    }

    public abstract void draw(GameDrawer gameDrawer);

    public abstract Integer GetDrawPrioritet();

    public abstract Vector2D getPosition();

    public abstract Vector2D getSize();

    public abstract boolean getIsVisible();
}
