package connector.protocol;

import connector.Gates;

/**
 * @see Gates
 * @see controller.AppController
 *  Listener of messages. If popMessage returns true, then message would be deleted.
 */

public interface ProtocolMessagesConductor {
    boolean applyMessage(ProtocolMessage message);
}
