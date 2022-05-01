package view.drawTools;

import tools.Vector2D;

import java.awt.*;

public class GameScaler {
    Point blackZone = new Point(100, 100);

    private Dimension screenSizeAfterShift;
    private double scalingCoefficient;
    private Point drawOffset;

    public GameScaler(Dimension screenSize, Point buildingSize) {
        updateSizes(screenSize, buildingSize);
    }

    public double getFromRealToGameLength(double length) {
        return length / scalingCoefficient;
    }

    public Vector2D getFromRealToGameCoordinate(Vector2D realPosition, int heigthOfTheObject) {
        return new Vector2D(
                blackZone.x/2+ drawOffset.x + (realPosition.x) / scalingCoefficient,
                blackZone.y/2+ (screenSizeAfterShift.height - drawOffset.y - (realPosition.y + heigthOfTheObject) / scalingCoefficient));

    }

    public void updateSizes(Dimension screenSize, Point buildingSize) {
        screenSizeAfterShift = new Dimension(screenSize.width - blackZone.x, screenSize.height - blackZone.y);
        scalingCoefficient = Math.max(
                ((double) buildingSize.x) / screenSizeAfterShift.width,
                ((double) buildingSize.y) / screenSizeAfterShift.height);
        Point sizeOfBuildingAfterRescale = new Point(
                (int) (((double) buildingSize.x) / scalingCoefficient),
                (int) (((double) buildingSize.y) / scalingCoefficient));
        drawOffset = new Point(
                (screenSizeAfterShift.width - sizeOfBuildingAfterRescale.x) / 2,
                (screenSizeAfterShift.height - sizeOfBuildingAfterRescale.y) / 2);

    }
}
