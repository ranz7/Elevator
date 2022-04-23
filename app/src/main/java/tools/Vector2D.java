package tools;

import lombok.NoArgsConstructor;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

/*
 * Personal Point class with usefull methods
 */
@NoArgsConstructor
public class Vector2D extends Point2D.Double implements Serializable {
    public static final double EPSILON = 0.0001;

    public Vector2D(double x, double y) {
        super(x, y);
    }

    public Vector2D(Point2D.Double position) {
        super(position.x, position.y);
    }

    public Vector2D(Point position) {
        super(position.x, position.y);
    }

    public static double distance(Point2D.Double positionA, Point2D.Double positionB) {
        return Math.sqrt((positionA.x - positionB.x) * (positionA.x - positionB.x)
                + (positionA.y - positionB.y) * (positionA.y - positionB.y));
    }

    public Vector2D getVectorTo(Point2D.Double destination) {
        return new Vector2D(destination.x - x, destination.y - y);
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D divide(double length) {
        return new Vector2D(x / length, y / length);
    }

    public Vector2D multiply(double length) {
        return new Vector2D(x * length, y * length);
    }

    public Vector2D add(Point2D.Double second) {
        return new Vector2D(x + second.x, y + second.y);
    }

    public Vector2D sub(Vector2D vectorB) {
        return new Vector2D(x - vectorB.x, y - vectorB.y);
    }

    public Vector2D trendTo(Vector2D destination, double length) {
        Vector2D moveDirection = new Vector2D(this).getVectorTo(destination);

        if (moveDirection.getLength() > EPSILON) {
            moveDirection = moveDirection.divide(moveDirection.getLength());
            moveDirection = moveDirection.multiply(length);
        }
        var first_vector = new Vector2D(this).add(moveDirection).getVectorTo(destination);
        var second_vector = new Vector2D(this).getVectorTo(destination);
        if (first_vector.x * second_vector.x <= EPSILON &&
                first_vector.y * second_vector.y <= EPSILON) {
            return destination;
        } else {
            return new Vector2D(this).add(moveDirection);
        }
    }
}
