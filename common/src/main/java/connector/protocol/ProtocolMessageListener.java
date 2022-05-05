package connector.protocol;

import java.io.Serializable;

/**
 * @see connector.Bridge
 * @see controller.AppController
 *  Listener of messages. If popMessage returns true, then message would be deleted.
 */

public interface ProtocolMessageListener {
    boolean popMessage(Protocol protocol, Serializable data);
}
