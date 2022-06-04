package model.planes.graphics;

import lombok.Getter;
import controller.Tickable;
import lombok.RequiredArgsConstructor;
import tools.Vector2D;

import java.awt.*;

@RequiredArgsConstructor
public class Scaler implements Tickable {
    private final Vector2D blackZone;

    private Vector2D screenSizeAfterShift;
    private Vector2D drawOffset = new Vector2D(0, 0);
    private double scalingCoefficient;

    // ZOOM DATA
    @Getter
    private double additionalZoomFinishValue = 1.;
    private double additionalZoomCurrentValue;

    private Vector2D additionalMoveFinishValue = new Vector2D(0, 0);
    private Vector2D additionalMove = new Vector2D(0, 0);

    Vector2D clickedPoint = new Vector2D(0, 0);
    double zoomScale = 1;

    public Scaler(Vector2D blackZone, Double zoomStartValue) {
        this.blackZone = blackZone;
        this.additionalZoomCurrentValue = zoomStartValue;
        updateSizes(new Vector2D(100, 100), new Vector2D(100, 100));
    }

    @Override
    public void tick(double deltaTime) {
        additionalZoomCurrentValue += (additionalZoomFinishValue - additionalZoomCurrentValue) / 20;
        additionalMove = additionalMove
                .add(additionalMoveFinishValue.sub(additionalMove).divide(10));
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

    private Vector2D screenSizeSave;

    public void updateSizes(Vector2D screenSize, Vector2D gameSize) {
        this.screenSizeSave = screenSize;
        clickedPoint = clickedPoint.sub(drawOffset).divide(scalingCoefficient); // NEED TO BE FIXED

        screenSizeAfterShift = new Vector2D(screenSize.getX() - blackZone.x, screenSize.getY() - blackZone.y);
        scalingCoefficient = gameSize.divide(screenSizeAfterShift).maxOfXY();
        Vector2D sizeOfBuildingAfterRescale = gameSize.divide(scalingCoefficient);
        drawOffset = screenSizeAfterShift.sub(sizeOfBuildingAfterRescale).divide(2);

        clickedPoint = clickedPoint.multiply(scalingCoefficient).add(drawOffset);
        if (zoomScale != 1) {
            updateZoomVector();
        }
    }

    public void updateGameSizes(Vector2D gameSize) {
        updateSizes(screenSizeSave, gameSize);
    }

    private void updateZoomVector() {
        isInitialised();
        additionalZoomFinishValue = zoomScale / Math.sqrt(scalingCoefficient);
        additionalMoveFinishValue = new Vector2D(0, -screenSizeAfterShift.y)
                .add(drawOffset.multiply(1 - additionalZoomFinishValue))
                .add(screenSizeAfterShift.multiply(additionalZoomFinishValue / 2))
                .add(new Vector2D(-1, 1).multiply(clickedPoint.sub(blackZone.divide(2)))
                );
    }

    private void isInitialised() {
        if (screenSizeAfterShift == null || scalingCoefficient < 0.01) {
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
