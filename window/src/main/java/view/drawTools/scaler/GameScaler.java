package view.drawTools.scaler;

import configs.GameScalerConfig;
import lombok.Getter;
import architecture.tickable.Tickable;
import tools.Vector2D;

import java.awt.*;

public class GameScaler implements Tickable {
    private final Vector2D blackZone = GameScalerConfig.blackZone;

    private Vector2D screenSizeAfterShift;
    private Vector2D drawOffset = new Vector2D(0, 0);
    private double scalingCoefficient;

    // ZOOM DATA
    @Getter
    private double additionalZoomFinishValue = 1.;
    private double additionalZoomCurrentValue = GameScalerConfig.zoomStartValue;

    private Vector2D additionalMoveFinishValue = new Vector2D(0, 0);
    private Vector2D additionalMove = new Vector2D(0, 0);

    Vector2D clickedPoint = new Vector2D(0, 0);
    double zoomScale = 1;

    @Override
    public void tick(double deltaTime) {
        additionalZoomCurrentValue += (additionalZoomFinishValue - additionalZoomCurrentValue) / 20;
        additionalMove = additionalMove
                .getAdded(additionalMoveFinishValue.getSubbed(additionalMove).getDivided(10));
    }

    public double getFromGameToRealLength(double length) {
        return length / (scalingCoefficient * additionalZoomCurrentValue);
    }

    public Vector2D getFromGameToRealCoordinate(Vector2D gameCoordinate, double heigthOfTheObject) {
        isInitialised();
        return new Vector2D(
                drawOffset.x
                        + gameCoordinate.x / (scalingCoefficient * additionalZoomCurrentValue)
                        + additionalMove.x / (additionalZoomCurrentValue) + blackZone.x / 2,
                -(drawOffset.y
                        + (gameCoordinate.y + heigthOfTheObject) / (scalingCoefficient * additionalZoomCurrentValue)
                        + (additionalMove.y) / (additionalZoomCurrentValue)) + screenSizeAfterShift.y + blackZone.y / 2);
    }

    public Vector2D getFromRealToGameCoordinate(Vector2D realPosition, int heigthOfTheObject) {
        isInitialised();
        return new Vector2D(
                (realPosition.x - drawOffset.x - blackZone.x / 2
                        - additionalMove.x / (additionalZoomCurrentValue)) * scalingCoefficient * additionalZoomCurrentValue,
                (-realPosition.y + blackZone.y / 2 + screenSizeAfterShift.y - drawOffset.y
                        - (additionalMove.y) / (additionalZoomCurrentValue)) * scalingCoefficient * additionalZoomCurrentValue
                        - heigthOfTheObject);
    }

    public void updateSizes(Dimension screenSize, Vector2D buildingSize) {
        clickedPoint = clickedPoint.getSubbed(drawOffset).getDivided(scalingCoefficient); // NEED TO BE FIXED

        screenSizeAfterShift = new Vector2D(screenSize.width - blackZone.x, screenSize.height - blackZone.y);
        scalingCoefficient = buildingSize.getDivided(screenSizeAfterShift).getMaxOfTwo();
        Vector2D sizeOfBuildingAfterRescale = buildingSize.getDivided(scalingCoefficient);
        drawOffset = screenSizeAfterShift.getSubbed(sizeOfBuildingAfterRescale).getDivided(2);

        clickedPoint = clickedPoint.getMultiplied(scalingCoefficient).getAdded(drawOffset);
        if (zoomScale != 1) {
            updateZoomVector();
        }
    }

    private void updateZoomVector() {
        isInitialised();
        additionalZoomFinishValue = zoomScale / Math.sqrt(scalingCoefficient);
        additionalMoveFinishValue = new Vector2D(0, -screenSizeAfterShift.y)
                .getAdded(drawOffset.getMultiplied(1 - additionalZoomFinishValue))
                .getAdded(screenSizeAfterShift.getMultiplied(additionalZoomFinishValue / 2))
                .getAdded(new Vector2D(-1, 1).getMultiplied(clickedPoint.getSubbed(blackZone.getDivided(2)))
                );
    }

    private void isInitialised() {
        if(screenSizeAfterShift==null || scalingCoefficient < 0.01){
            throw new RuntimeException("Scaler is not initialized Or scaling coefficient is to small." +
                    " Do update before use of draw functions and check if coefficient is set properly.");
        }
    }

    public void zoomIn(Vector2D point, double zoomScale) {
        this.zoomScale = zoomScale;
        clickedPoint = point;
        updateZoomVector();
    }

    public void zoomOut() {
        additionalMoveFinishValue = new Vector2D(0, 0);
        additionalZoomFinishValue = 1;
        this.zoomScale = 1;
    }

}
