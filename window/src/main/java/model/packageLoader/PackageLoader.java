package model.packageLoader;

import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableCreature;
import drawable.abstracts.DrawableRemoteCreature;
import model.DatabaseOf;
import model.GameMap;
import model.Transport;
import protocol.special.CreatureData;
import protocol.special.GameMapCompactData;
import tools.Pair;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class PackageLoader {
    public static void applyArrivedData(GameMapCompactData modelOfRemoteMap, GameMap map) {
        DatabaseOf<Drawable> modelOfMap = map.getLocalDataBase();

        List<CreatureData> arrivedCreatures = modelOfRemoteMap.parentIdClassTypeObject;

        List<Pair<Integer, DrawableRemoteCreature>> localCreatures = modelOfMap.streamWithParentsOf(DrawableRemoteCreature.class).toList();

        // Erase
        localCreatures.forEach(localCreature -> localCreature.getSecond().setDead(true));

        //  Update
        arrivedCreatures.forEach(
                creatureData -> {
                    var findWithTheSameId = localCreatures.stream()
                            .filter(localCreature -> localCreature.getSecond().getId() == creatureData.getId())
                            .findFirst();
                    if (findWithTheSameId.isPresent()) {
                        if (!Objects.equals(findWithTheSameId.get().getFirst(), creatureData.getIdOfParent())) {
                            // change parent
                            var newParent = modelOfMap.get(creatureData.getIdOfParent(), Transport.class);
                            modelOfMap.moveCreatureInto(creatureData.getId(), newParent.getSecond());
                        }
                        findWithTheSameId.get().getSecond().set(creatureData);
                        findWithTheSameId.get().getSecond().setDead(false);
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

                    ((Transport) drawable).add(new DrawableCreatureData(creatureData));
                }
        );
    }

}
