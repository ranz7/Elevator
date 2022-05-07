package configs;

import lombok.AllArgsConstructor;
import tools.Vector2D;

import java.io.Serializable;

/**
 * Contains all information about application
 * <p>
 * This object need to be sent for client first, so that the client can initialize his database
 * according to this fields.
 * </p>
 */

@AllArgsConstructor
public class RemoteConfig implements Serializable {
    public final Vector2D buildingSize;
    public final Vector2D elevatorSize;
    public final Vector2D customerSize;
    public final long elevatoropenclosetime;
    public final int elevatorsCount;
    public final int floorsCount;
    public final double buttonRelativePosition;
    public final double GAME_SPEED;
    public final double VERSION;

}
