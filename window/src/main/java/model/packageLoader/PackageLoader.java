package model.packageLoader;

import drawable.abstracts.Drawable;
import protocol.special.GameMapCompactData;
import model.DatabaseOf;
import drawable.abstracts.DrawableRemoteCreature;
import model.objects.Creature;
import tools.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PackageLoader {
    public static void applyArivedData(GameMapCompactData arivedData, DatabaseOf<Drawable> localModel) {
        List<Pair<Integer, Creature>> arrivedCreatures = arivedData.parentIdAndObjects;

        // erase or Update
        localModel.streamOf(DrawableRemoteCreature.class).forEach(
                drawableRemoteCreature -> {
                    int creatureId = drawableRemoteCreature.getId();
                    var creaturePair = arrivedCreatures.stream().filter(
                                    creaturePairTmp -> creaturePairTmp.getSecond().getId() == creatureId)
                            .findFirst();
                    if (creaturePair.isEmpty()) {
                        drawableRemoteCreature.setDead(true);
                    } else {
                        drawableRemoteCreature.set(arrivedCreatures.get(creatureId).getSecond());
                        arrivedCreatures.remove(creaturePair.get());
                    }
                });
        // Create
        arrivedCreatures.forEach(
                parentIdAndCreature -> {
                    var parentId = parentIdAndCreature.getFirst();
                    var creature = parentIdAndCreature.getSecond();
                     localModel.
                             get(parentId).
                             add(creature);
                });
    }


}
