package model;

import configs.ConnectionSettings;
import model.objects.AppGameMap;
import settings.LocalObjectsSettings;
import configs.ConnectionEstalblishConfig;
import lombok.Getter;
import connector.protocol.GameMapCompactData;

import java.io.Serializable;

/*
 * Class to store all objects.
 */

public class AppModel {
    @Getter
    private final LocalObjectsSettings localObjectsSettings = new LocalObjectsSettings();
    AppGameMap appGameMap = new AppGameMap(localObjectsSettings);

    public GameMapCompactData sendMap() {
        return new GameMapCompactData(appGameMap.getDataBase().toIdAndCreaturesList());
    }

    public Serializable createMainInitializationSettingsToSend(double gameSpeed) {
        return new ConnectionEstalblishConfig(
                localObjectsSettings.elevatorOpenCloseTime(),
                localObjectsSettings.buttonRelativePosition(),
                gameSpeed,
                ConnectionSettings.VERSION);
    }
}
