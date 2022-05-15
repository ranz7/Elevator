package connector.protocol;

import configs.ConnectionSettings;
import lombok.RequiredArgsConstructor;
import model.objects.Creature;
import tools.Pair;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;


/**
 * Contains all data about creatures to send.
 * <p>
 * This data is sending SSPS times at second
 *
 * @see ConnectionSettings
 * </p>
 */
@RequiredArgsConstructor
public class GameMapCompactData implements Serializable {

    final List<Pair<Long, Creature>> parentIdAndObjects;

    Stream<Pair<Long, Creature>> stream() {
        return parentIdAndObjects.stream();
    }
}
