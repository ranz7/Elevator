package model.planes;

import controller.Tickable;
import drawable.abstracts.Drawable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;
import view.gui.windowReacts.MouseReact;
import model.planes.graphics.Painter;
import model.planes.graphics.Scaler;

import java.awt.*;

@RequiredArgsConstructor
public abstract class Plane implements Tickable, MouseReact {

    @Getter
    @Setter
    private boolean isActive = true;

    @Getter
    protected final LocalDrawSetting settings;

    @Getter
    private final Scaler scaler = new Scaler();
    private final Painter drawer = new Painter(scaler);

    public boolean zoomedIn() {
        return scaler.getAdditionalZoomFinishValue() == 1.;
    }

    public void zoomOut() {
        scaler.zoomOut();
    }

    public void zoomIn(Vector2D point, double zoomScale) {
        scaler.zoomIn(point, zoomScale);
    }

    @Override
    public void tick(double deltaTime) {
        scaler.tick(deltaTime);
    }

    public void draw(Graphics g) {
        drawer.prepareDrawer(g);
        getDrawable().draw(new Vector2D(0, 0), drawer);
    }

    protected abstract Drawable getDrawable();

    public abstract int getId();

    public abstract void resize(Dimension size);
}
