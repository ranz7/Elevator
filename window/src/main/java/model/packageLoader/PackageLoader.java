package model.packageLoader;

import drawable.abstracts.Drawable;
import model.GameMap;
import protocol.special.CreatureData;
import protocol.special.CreatureType;
import protocol.special.GameMapCompactData;
import model.DatabaseOf;
import drawable.abstracts.DrawableRemoteCreature;

import java.util.List;

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
                    modelOfMap.get(creatureData.getIdOfParent()).add(new DrawableCreatureData(creatureData));
                }
        );
    }

}
