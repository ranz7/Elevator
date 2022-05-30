package model.packageLoader;

import drawable.abstracts.Drawable;
import protocol.special.GameMapCompactData;
import model.DatabaseOf;
import drawable.abstracts.DrawableRemoteCreature;
import model.objects.Creature;
import tools.Pair;

import java.util.Map;
import java.util.stream.Collectors;

public class PackageLoader {
    public static void applyArivedData(GameMapCompactData arivedData, DatabaseOf<Drawable> localModel) {
        // idOfCreature , <ParentId, creature >
        Map<Integer, Pair<Integer, Creature>> idToCreature = arivedData.parentIdAndObjects.stream()
                .collect(Collectors.toMap(
                        idOfParentAndCreature -> idOfParentAndCreature.getSecond().getId(),
                        idOfParentAndCreature -> idOfParentAndCreature
                ));

        // erase
        localModel.streamOf(DrawableRemoteCreature.class).forEach(
                drawableRemoteCreature -> {
                    Integer id = drawableRemoteCreature.getId();
                    boolean containsKey = idToCreature.containsKey(id);
                    if (!containsKey) {
                        drawableRemoteCreature.setDead(true);
                    } else {
                        drawableRemoteCreature.set(idToCreature.get(id).getSecond());
                        idToCreature.remove(id);
                    }
                });
        idToCreature.forEach((id, parentIdAndCreature) -> {
            (localModel.get(parentIdAndCreature.getFirst())).getLocalDataBase().add(parentIdAndCreature.getSecond());
        });
    }


}
