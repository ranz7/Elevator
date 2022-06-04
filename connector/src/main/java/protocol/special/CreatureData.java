package protocol.special;

import lombok.Getter;
import model.objects.Creature;

public class CreatureData extends Creature {
    @Getter
    private final  CreatureType creatureType;
    @Getter
    private final Integer idOfParent;

    public CreatureData(Creature creatureA, Integer idOfParent, CreatureType creatureType) {
        super(creatureA);
        this.idOfParent = idOfParent;
        this.creatureType = creatureType;
    }
}
