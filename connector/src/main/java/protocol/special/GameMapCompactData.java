package protocol.special;

import configs.ConnectionSettings;
import configs.RoomPrepareCompactData;
import lombok.RequiredArgsConstructor;
import model.objects.Creature;
import tools.Pair;
import tools.Trio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Contains all data about creatures to send.
 * <p>
 *
 * @see ConnectionSettings
 * </p>
 */
@RequiredArgsConstructor
public class GameMapCompactData implements Serializable {
    public final ArrayList<CreatureData> parentIdClassTypeObject;
    public final RoomPrepareCompactData roomData;
}
