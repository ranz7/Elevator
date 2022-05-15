package model.objects;

import model.DataBaseOfCreatures;
import model.objects.building.Building;
import settings.LocalObjectsSettings;

public class GameMap {
    DataBaseOfCreatures dataBase;

    public GameMap(LocalObjectsSettings localObjectsSettings) {
        dataBase.add(new Building(localObjectsSettings, this));
    }
}
