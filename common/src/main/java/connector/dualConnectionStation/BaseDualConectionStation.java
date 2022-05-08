package connector.dualConnectionStation;

import connector.dualConnectionStation.download.Downlink;
import connector.dualConnectionStation.upload.Uplink;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base station is a class that can get write information to Downlink
 * and read information from Uplink
 *
 * @see Uplink
 * @see Downlink
 */
public abstract class BaseDualConectionStation implements Uplink {
    @Setter
    protected Downlink downlink;

    protected AtomicBoolean isDisconnect = new AtomicBoolean(true);

    public abstract void start();

    public abstract boolean isDisconnect();
}
