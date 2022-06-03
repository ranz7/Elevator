package model.packageLoader;

import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableRemoteCreature;
import model.DatabaseOf;
import model.GameMap;
import model.Transport;
import protocol.special.CreatureData;
import protocol.special.CreatureType;
import protocol.special.GameMapCompactData;

import java.util.List;
import java.util.NoSuchElementException;

public class PackageLoader {
    public static void applyArrivedData(GameMapCompactData modelOfRemoteMap, GameMap map) {
        DatabaseOf<Drawable> modelOfMap = map.getLocalDataBase();

        List<CreatureData> arrivedCreatures = modelOfRemoteMap.parentIdClassTypeObject;

        List<DrawableRemoteCreature> localCreatures = modelOfMap.streamOf(DrawableRemoteCreature.class).toList();

        // Erase
        localCreatures.forEach(localCreature -> localCreature.setDead(true));

        //  Update
        arrivedCreatures.forEach(
                creatureData -> {
                    if (creatureData.getCreatureType() == CreatureType.GAME_MAP) {
                        map.set(creatureData); // Game Map can have another mapId, so we need to set it by hands
                        return;
                    }
                    var findWithTheSameId = localCreatures.stream()
                            .filter(localCreature -> localCreature.getId() == creatureData.getId())
                            .findFirst();
                    if (findWithTheSameId.isPresent()) {
                        findWithTheSameId.get().set(creatureData);
                        findWithTheSameId.get().setDead(false);
                        return;
                    }

                    // Create
                    Drawable drawable;
                    try {
                        drawable = modelOfMap.get(creatureData.getIdOfParent()).getSecond();
                    } catch (NoSuchElementException e) {
                        throw new RuntimeException("Cannot find with id: " + creatureData.getIdOfParent()
                                + " an object: " + creatureData.getCreatureType());
                    }
                    try {
                        ((Transport) drawable).add(new DrawableCreatureData(creatureData));
                    } catch (ClassCastException e) {
                        throw new RuntimeException("Found object" + drawable + " is not an transport");
                    }
                }
        );
    }

}
