package protocol;

import gates.Gates;

/**
 * @see Gates
 * @see AppController
 *  Listener of messages. If popMessage returns true, then message would be deleted.
 */
public interface ProtocolMessagesController {
    boolean applyMessage(ProtocolMessage message);
}