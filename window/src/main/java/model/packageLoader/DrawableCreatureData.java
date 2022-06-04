package model.packageLoader;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import lombok.Getter;
import protocol.special.CreatureData;
import protocol.special.CreatureType;

public class DrawableCreatureData extends DrawableCreature {
    @Getter
    private final CreatureType creatureType;

    public DrawableCreatureData(CreatureData arrivedCreatureData) {
        super(arrivedCreatureData.getPosition(), arrivedCreatureData.getSize(), null, null);
        creatureType = arrivedCreatureData.getCreatureType();
    }

    @Override
    public DrawCenter getDrawCenter() {
        return null;
    }

    @Override
    public int getDrawPriority() {
        return 0;
    }
}
