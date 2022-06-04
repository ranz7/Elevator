package configs;

import tools.Vector2D;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains all information about application
 * <p>
 * This object need to be sent for client first, so that the client can initialize his database
 * according to this fields.
 * </p>
 */

public record RoomPrepareCompactData(double version, long elevatorOpenCloseTime, Vector2D customerSize,
                                     double gameSpeed, int roomId) implements Serializable {
}
