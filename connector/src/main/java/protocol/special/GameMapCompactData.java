package protocol.special;

import configs.ConnectionSettings;
import lombok.RequiredArgsConstructor;
import model.objects.Creature;
import tools.Pair;

import java.io.Serializable;
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
    public final List<Pair<Integer, Creature>> parentIdAndObjects;
}
