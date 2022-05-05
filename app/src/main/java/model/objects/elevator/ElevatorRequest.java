package model.objects.elevator;

import common.tools.Vector2D;

public record ElevatorRequest(Vector2D button_position, boolean is_go_up) {
}
