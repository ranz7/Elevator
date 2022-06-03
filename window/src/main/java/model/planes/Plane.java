package model.planes;

import controller.Tickable;
import drawable.abstracts.Drawable;
import drawable.buttons.ClickableButton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import model.DatabaseOf;
import model.objects.CreatureInterface;
import model.planes.graphics.Scaler;
import settings.localDraw.LocalDrawSetting;
import tools.Pair;
import tools.Vector2D;
import view.gui.windowReacts.MouseReact;
import model.planes.graphics.Painter;

import java.awt.*;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class Plane implements Tickable, MouseReact {
    @Getter
    @Setter
    private boolean isActive = true;

    @Getter
    protected final LocalDrawSetting settings;
    private final Painter painter;

    Scaler getScaler() {
        return painter.getScaler();
    }

    public boolean zoomedIn() {
        return painter.getScaler().getAdditionalZoomFinishValue() == 1.;
    }

    public void zoomOut() {
        painter.getScaler().zoomOut();
    }

    public void zoomIn(Vector2D point, double zoomScale) {
        painter.getScaler().zoomIn(point, zoomScale);
    }

    @Override
    public void tick(double deltaTime) {
        painter.getScaler().tick(deltaTime);
        getLocalDataBase().tick(deltaTime * getTimeSpeed());
        getLocalDataBase().removeIf(CreatureInterface::isDead);
        updateDrawingObjectsForThreadSafety();
    }

    private List<Pair<Vector2D, Drawable>> lastObjectsState;

    private void updateDrawingObjectsForThreadSafety() {
        lastObjectsState = getLocalDataBase()
                .stream()
                .sorted(Comparator.comparingInt(
                        drawableObjet -> drawableObjet.getSecond().getDrawPriority())).collect(Collectors.toList());
    }

    protected abstract double getTimeSpeed();

    public void draw(Graphics g) {
        painter.prepareDrawer(g);
        if(lastObjectsState!=null)
        lastObjectsState.forEach(positionAndObject -> {
            positionAndObject.getSecond().draw(positionAndObject.getFirst(), painter);
        });
    }

    protected abstract DatabaseOf<Drawable> getLocalDataBase();

    public abstract void resize(Vector2D size);

    public void mousePositionUpdate(Point mouseLocation) {
        var gamePosition = getScaler().getFromRealToGameCoordinate(new Vector2D(mouseLocation), 0);
        getLocalDataBase().streamOf(ClickableButton.class).forEach(
                clickableButton -> clickableButton.mousePositionUpdate(gamePosition));
    }
}
