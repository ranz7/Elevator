package model;

import model.objects.CreatureInterface;
import tools.Vector2D;

public interface Transportable<BaseCreatureObject extends CreatureInterface> {
    void setTransport(Transport<BaseCreatureObject> transport);

    Transport<BaseCreatureObject> getTransport();

    Vector2D getPosition();

    void applyDelta(Vector2D deltaInParentPositions);
}
