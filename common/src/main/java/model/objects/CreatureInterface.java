package model.objects;

import controller.Tickable;
import tools.Vector2D;

public interface CreatureInterface extends Tickable {
     void applyDelta(Vector2D deltaInParentPositions);
     Vector2D getPosition();
     int getId();
     boolean isDead();
}
