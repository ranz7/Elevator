package configs;

import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * Contains all information about application
 * <p>
 * This object need to be sent for client first, so that the client can initialize his database
 * according to this fields.
 * </p>
 */

@AllArgsConstructor
public class ConnectionEstalblishConfig implements Serializable {
    public final long elevatorOpenCloseTime;
    public final double buttonRelativePosition;
    public final double gameSpeed;
    public final double version;

}
