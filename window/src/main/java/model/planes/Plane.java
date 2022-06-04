package model.planes;

import controller.Tickable;
import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableCreature;
import drawable.buttons.ClickableButton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import model.DatabaseOf;
import model.Transport;
import model.planes.graphics.Scaler;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;
import view.gui.windowReacts.MouseReact;
import model.planes.graphics.Painter;

import java.awt.*;
import java.util.Comparator;

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
        getLocalDataBase().tick(deltaTime);
    }

    public void draw(Graphics g) {
        painter.prepareDrawer(g);
        var objectsAndRelativePositions = getLocalDataBase().toAbsolutePositionAndObjects();
        objectsAndRelativePositions.sort(Comparator.comparingInt(drawableObjet -> drawableObjet.getSecond().getDrawPrioritet()));
        objectsAndRelativePositions.forEach(
                positionAndObject -> {
                    positionAndObject.getSecond().draw(positionAndObject.getFirst(), painter);
                });
        painter.drawBlackSpaces();
    }

    protected abstract DatabaseOf<Drawable> getLocalDataBase();

    public abstract int getId();

    public abstract void resize(Dimension size);

    public void mousePositionUpdate(Point mouseLocation) {
        getLocalDataBase().streamOf(ClickableButton.class).forEach(
                clickableButton -> clickableButton.mousePositionUpdate(
                        getScaler().getFromRealToGameCoordinate(
                                new Vector2D(mouseLocation), 0)
                ));
    }

    public void mouseClicked() {
        getLocalDataBase().streamOf(ClickableButton.class).filter(ClickableButton::isHovered)
                .forEach(ClickableButton::execute);
    }

}
