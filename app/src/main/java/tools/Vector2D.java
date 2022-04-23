package tools;

import lombok.NoArgsConstructor;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

/*
 * Personal Point class with useful methods
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

}
