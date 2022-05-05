package connector.protocol;

import connector.Gates;

/**
 * @see Gates
 * @see controller.AppController
 *  Listener of messages. If popMessage returns true, then message would be deleted.
 */

public interface ProtocolMessageListener {
    boolean popMessage(ProtocolMessage message);
}
