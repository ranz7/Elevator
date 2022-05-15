package model.packageLoader;

import connector.protocol.GameMapCompactData;
import model.DataBaseOfDrawableCreatures;
import settings.CombienedDrawSettings;
import drawable.abstracts.DrawableRemoteCreature;
import drawable.concretes.customer.DrawableCustomer;
import model.objects.Creature;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PackageLoader {
    public static void applyArivedData(
            GameMapCompactData data, DataBaseOfDrawableCreatures model, CombienedDrawSettings settings
    ) {
        Map<Long, Creature> idToCreature = model.streamOf(DrawableRemoteCreature.class)
                .collect(Collectors.toMap(DrawableRemoteCreature::getId, Function.identity()));
        // erase
        model.removeIf(creature -> {
            if (creature instanceof DrawableRemoteCreature) {
                Long id = ((DrawableRemoteCreature) creature).getId();
                boolean containsKey = idToCreature.containsKey(id);
                if (containsKey) {
                    idToCreature.remove(id);
                }
                return containsKey;
            } else {
                return false;
            }
        });
        // update
        model.streamOf(DrawableRemoteCreature.class)
                .forEach(remoteCreature -> {
                            remoteCreature.set(idToCreature.get(remoteCreature.getId()));
                        }
                );

        // create
        customers.forEach(
                creatureA -> {
                    if (drawableCustomers.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())) {
                        drawableCustomers.add(new DrawableCustomer(creatureA, settings));
                    }
                }
        );
    }


}
