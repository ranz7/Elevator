package tools;

import lombok.NoArgsConstructor;

import java.awt.geom.Point2D;

/*
 * Personal Point class with useful methods
 */
@NoArgsConstructor
public class Vector2D extends Point2D.Double {
    public static final double EPSILON = 0.0001;

    public Vector2D(double x, double y) {
        super(x, y);
    }

    public Vector2D(Point2D.Double position) {
        this(position.x, position.y);
    }

    public static double distance(Point2D.Double positionA, Point2D.Double positionB) {
        return Math.sqrt(Math.pow(positionA.x - positionB.x, 2) + Math.pow(positionA.y - positionB.y, 2));
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

    public Vector2D getNearest(Vector2D vectorA, Vector2D vectorB) {
        if (Vector2D.distance(this, vectorA) <
                Vector2D.distance(this, vectorB)) {
            return vectorA;
        }
        return vectorB;
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
