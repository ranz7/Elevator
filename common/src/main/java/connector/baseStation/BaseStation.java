package connector.baseStation;

import connector.Gates;
import connector.baseStation.download.Uplink;

import java.util.concurrent.atomic.AtomicBoolean;

/**
* Base station is a class that can get write information to Downlink
* and read information from Uplink
* @see Downlink
* @see Uplink
 */
public abstract class BaseStation implements Downlink {
    protected Uplink uplink;

    protected AtomicBoolean isStoped = new AtomicBoolean(false);

    public void setDownlink(Gates downlink) {
        this.uplink = downlink;
    }

    public abstract void start();

    public abstract boolean isStopped();
}
