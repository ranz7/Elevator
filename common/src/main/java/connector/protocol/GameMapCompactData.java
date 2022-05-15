package connector.protocol;

import configs.ConnectionSettings;
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
public class SendableCreaturesList implements Serializable {

    List<Pair<Long, Object>> parentIdAndObjects = new LinkedList<>();
    Stream<Pair<Long, Object>> stream(){
        return parentIdAndObjects.stream();
    };
}
