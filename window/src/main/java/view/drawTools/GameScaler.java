package view.drawTools;

import lombok.Getter;
import tools.Vector2D;

import java.awt.*;

public class GameScaler {
    Vector2D blackZone = new Vector2D(100, 100);
    private Vector2D screenSizeAfterShift = new Vector2D(0, 0);
    private double scalingCoefficient;
    private Vector2D drawOffset = new Vector2D(0, 0);

    // SCALE DATA
    @Getter
    private double additionalZoomFinishValue = 1.;
    private double additionalZoomCurrentValue = 1.;

    private Vector2D additionalMoveFinishValue = new Vector2D(0, 0);
    private Vector2D additionalMove = new Vector2D(0, 0);

    Vector2D clickedPoint = new Vector2D(0, 0);
    double zoomScale = 1;


    public void tick() {
        additionalZoomCurrentValue += (additionalZoomFinishValue - additionalZoomCurrentValue) / 10;
        additionalMove = additionalMove
                .getAdded(additionalMoveFinishValue.getSubbed(additionalMove).getDivided(10));
    }

    public GameScaler(Dimension screenSize, Vector2D buildingSize) {
        updateSizes(screenSize, buildingSize);
    }

    public double getFromRealToGameLength(double length) {
        return length / (scalingCoefficient * additionalZoomCurrentValue);
    }

    public Vector2D getFromRealToGameCoordinate(Vector2D realPosition, int heigthOfTheObject) {
        return new Vector2D(
                drawOffset.x
                        + realPosition.x / (scalingCoefficient * additionalZoomCurrentValue)
                        + additionalMove.x / (additionalZoomCurrentValue),
                screenSizeAfterShift.y - drawOffset.y
                        - (realPosition.y + heigthOfTheObject) / (scalingCoefficient * additionalZoomCurrentValue)
                        - (additionalMove.y) / (additionalZoomCurrentValue))
                .getAdded(blackZone.getDivided(2));

    }


    public void updateSizes(Dimension screenSize, Vector2D buildingSize) {
        clickedPoint = clickedPoint.getSubbed(drawOffset); // NEED TO BE FIXED

        screenSizeAfterShift = new Vector2D(screenSize.width - blackZone.x, screenSize.height - blackZone.y);
        scalingCoefficient = buildingSize.getDivided(screenSizeAfterShift).getMaxOfTwo();
        Vector2D sizeOfBuildingAfterRescale = buildingSize.getDivided(scalingCoefficient);
        drawOffset = screenSizeAfterShift.getSubbed(sizeOfBuildingAfterRescale).getDivided(2);

        clickedPoint = clickedPoint.getAdded(drawOffset);
        if (zoomScale != 1) {
            updateZoomVector();
        }
    }

    private void updateZoomVector() {
        additionalZoomFinishValue = zoomScale / Math.sqrt(scalingCoefficient);
        additionalMoveFinishValue = new Vector2D(0, -screenSizeAfterShift.y)
                .getAdded(drawOffset.getMultiplied(1 - additionalZoomFinishValue))
                .getAdded(screenSizeAfterShift.getMultiplied(additionalZoomFinishValue / 2))
                .getAdded(new Vector2D(-1, 1).getMultiplied(clickedPoint.getSubbed(blackZone.getDivided(2))));
    }


    public void zoomIn(Vector2D point, double zoomScale) {
        clickedPoint = point;
        this.zoomScale = zoomScale;
        updateZoomVector();
    }

    public void zoomOut() {
        additionalZoomFinishValue = 1;
        this.zoomScale = 1;
        additionalMoveFinishValue = new Vector2D(0, 0);
    }
}
