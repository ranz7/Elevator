package model;

import model.objects.CreatureInterface;

public interface Transport<BaseCreatureObject extends CreatureInterface> {
    void add(BaseCreatureObject creatureObject);

    DatabaseOf<BaseCreatureObject> getLocalDataBase();

    int getId();
}
