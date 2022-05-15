package model.objects;

import model.DataBaseOfCreatures;
import model.objects.building.Building;
import settings.LocalObjectsSettings;
import tools.Vector2D;

public class AppGameMap extends Creature {
    private final DataBaseOfCreatures dataBase = new DataBaseOfCreatures(this);

    public AppGameMap(LocalObjectsSettings localObjectsSettings) {
        super(new Vector2D(0, 0), new Vector2D(10, 10));
        dataBase.add(new Building(new Vector2D(0, 0), localObjectsSettings, this));
    }

    public DataBaseOfCreatures getDataBase() {
        return dataBase;
    }
}
