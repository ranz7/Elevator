package model.objects.elevator;

import java.awt.geom.Point2D;

public record ElevatorRequest(Point2D button_position, boolean is_go_up) {
}
