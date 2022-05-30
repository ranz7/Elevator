package model.objects.elevator;

import tools.Vector2D;

public record ElevatorRequest(Integer floorNum, Vector2D buttonPosition, boolean isGoUp) {
}
