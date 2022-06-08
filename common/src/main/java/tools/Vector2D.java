package tools;

import lombok.NoArgsConstructor;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

/*
 * Personal Point class with useful methods
 */
@NoArgsConstructor
public class Vector2D extends Point2D.Double {
    public static final double EPSILON = 0.0001;
    public static final Vector2D Up = new Vector2D(0, 1);
    public static final Vector2D Left = new Vector2D(-1, 0);
    public static final Vector2D Right = new Vector2D(1, 0);
    public static final Vector2D Down = new Vector2D(0, -1);

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

    public Vector2D(Dimension size) {
        this(size.width, size.height);
    }

    public static Vector2D getBetween(Vector2D A, Vector2D B, double coef) {
        return A.multiply(coef).add(B.multiply(1 - coef));
    }

    public static Vector2D Random() {
        return new Vector2D(new Random().nextDouble()-0.5, new Random().nextDouble()-0.5);
    }

    public double distance(Point2D.Double positionA) {
        return Math.sqrt(Math.pow(positionA.x - x, 2) + Math.pow(positionA.y - y, 2));
    }

    public Vector2D getTo(Vector2D destination) {
        return new Vector2D(destination.x - x, destination.y - y);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D divide(double length) {
        return new Vector2D(x / length, y / length);
    }

    public Vector2D multiply(double length) {
        return new Vector2D(x * length, y * length);
    }

    public Vector2D add(Vector2D second) {
        return new Vector2D(x + second.x, y + second.y);
    }

    public Vector2D add(double add) {
        return new Vector2D(x + add, y + add);
    }

    public Vector2D sub(double sub) {
        return new Vector2D(x + sub, y + sub);
    }

    public Vector2D sub(Vector2D vectorB) {
        return new Vector2D(x - vectorB.x, y - vectorB.y);
    }

    public Vector2D getNearest(Vector2D vectorA, Vector2D vectorB) {
        return distance(vectorA) < distance(vectorB) ? vectorA : vectorB;
    }

    public Vector2D getShiftedByDistance(Vector2D destination, double distance) {
        Vector2D moveDirection = getTo(destination);

        if (moveDirection.length() > EPSILON) {
            moveDirection = moveDirection.changeLength(distance);
        }

        var vectorToDestinationAfterShift = add(moveDirection).getTo(destination);
        var vectorToDestination = getTo(destination);

        if (vectorToDestinationAfterShift.x * vectorToDestination.x <= EPSILON &&
                vectorToDestinationAfterShift.y * vectorToDestination.y <= EPSILON) {
            return destination;
        } else {
            return add(moveDirection);
        }
    }

    private Vector2D changeLength(double length) {
        return makeUnit().multiply(length);
    }

    private Vector2D makeUnit() {
        return divide(length());
    }

    public Point toPoint() {
        return new Point((int) x, (int) y);
    }

    public Vector2D multiply(Vector2D multiply) {
        return new Vector2D(x * multiply.x, y * multiply.y);
    }

    public Vector2D divide(Vector2D division) {
        return new Vector2D(x / division.x, y / division.y);
    }

    public double maxOfXY() {
        return Math.max(x, y);
    }

    public Vector2D addByX(double x) {
        return new Vector2D(this.x + x, y);
    }

    public Vector2D addByY(double y) {
        return new Vector2D(x, this.y + y);
    }

    public Vector2D divideByX(double x) {
        return new Vector2D(this.x / x, this.y);
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

    public Vector2D set(Vector2D position) {
        this.x = position.x;
        this.y = position.y;
        return this;
    }

    public Vector2D setX(double newX) {
        this.x = newX;
        return this;
    }


    public Vector2D setY(double newY) {
        this.y = newY;
        return this;
    }

    public Vector2D withX(double newX) {
        return new Vector2D(newX, y);
    }


    public Vector2D withY(double newY) {
        return new Vector2D(x, newY);
    }

    public boolean isZero() {
        return this.equals(new Vector2D(0, 0));
    }

    public Vector2D multiplyY(double multiplyBy) {
        return new Vector2D(x, y * multiplyBy);
    }

    public Vector2D multiplyX(double multiplyBy) {
        return new Vector2D(x * multiplyBy, y);
    }

    public boolean isInside(Vector2D position, Vector2D square) {
        return position.x < x && position.x + square.x > x
                && position.y < y && position.y + square.y > y;
    }

    public boolean isInsideEllipse(Vector2D position, Vector2D ellipse) {
        position = position.add(ellipse.divide(2));
        double p = (Math.pow((x - position.x), 2) / Math.pow(ellipse.y / 2, 2))
                + (Math.pow((y - position.y), 2) / Math.pow(ellipse.x / 2, 2));

        return p <= 1;
    }
}
