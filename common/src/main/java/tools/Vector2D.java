package tools;

import lombok.NoArgsConstructor;

import java.awt.*;
import java.awt.geom.Point2D;

/*
 * Personal Point class with useful methods
 */
@NoArgsConstructor
public class Vector2D extends Point2D.Double {
    public static final double EPSILON = 0.0001;
    public static final Vector2D North = new Vector2D(0, 1);
    public static final Vector2D West = new Vector2D(-1, 0);
    public static final Vector2D East = new Vector2D(1, 0);
    public static final Vector2D South = new Vector2D(0, -1);

    public Vector2D(double x, double y) {
        super(x, y);
    }

    public Vector2D(Vector2D position) {
        this(position.x, position.y);
    }

    public Vector2D(Point2D.Double position) {
        this(position.x, position.y);
    }

    public Vector2D(Point point) {
        this(point.x, point.y);
    }

    public double distanceTo(Point2D.Double positionA) {
        return Math.sqrt(Math.pow(positionA.x - x, 2) + Math.pow(positionA.y - y, 2));
    }

    public Vector2D getVectorTo(Point2D.Double destination) {
        return new Vector2D(destination.x - x, destination.y - y);
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D getDivided(double length) {
        return new Vector2D(x / length, y / length);
    }

    public Vector2D getMultiplied(double length) {
        return new Vector2D(x * length, y * length);
    }

    public Vector2D getAdded(Vector2D second) {
        return new Vector2D(x + second.x, y + second.y);
    }

    public Vector2D getAdded(double add) {
        return new Vector2D(x + add, y + add);
    }

    public Vector2D getSubbed(Vector2D vectorB) {
        return new Vector2D(x - vectorB.x, y - vectorB.y);
    }

    public Vector2D getNearest(Vector2D vectorA, Vector2D vectorB) {
        return distanceTo(vectorA) < distanceTo(vectorB) ? vectorA : vectorB;
    }

    public Vector2D getShiftedByDistance(Vector2D destination, double distance) {
        Vector2D moveDirection = getVectorTo(destination);

        if (moveDirection.getLength() > EPSILON) {
            moveDirection = moveDirection.changeLength(distance);
        }

        var vectorToDestinationAfterShift = getAdded(moveDirection).getVectorTo(destination);
        var vectorToDestination = getVectorTo(destination);

        if (vectorToDestinationAfterShift.x * vectorToDestination.x <= EPSILON &&
                vectorToDestinationAfterShift.y * vectorToDestination.y <= EPSILON) {
            return destination;
        } else {
            return getAdded(moveDirection);
        }
    }

    private Vector2D changeLength(double length) {
        return makeUnit().getMultiplied(length);
    }

    private Vector2D makeUnit() {
        return getDivided(getLength());
    }

    public Point toPoint() {
        return new Point((int) x, (int) y);
    }

    public Double getMultiplied(Vector2D multiply) {
        return new Vector2D(x * multiply.x, y * multiply.y);
    }

    public Vector2D getDivided(Vector2D division) {
        return new Vector2D(x / division.x, y / division.y);
    }

    public double getMaxOfTwo() {
        return Math.max(x, y);
    }

    public Vector2D getAddedX(double x) {
        return new Vector2D(this.x + x, y);
    }

    public Vector2D getAddedY(double y) {
        return new Vector2D(x, this.y + y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (null == obj)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vector2D vector2D = (Vector2D) obj;
        // field comparison
        return Math.abs(x - vector2D.x) < EPSILON && Math.abs(y - vector2D.y) < EPSILON;
    }
}
