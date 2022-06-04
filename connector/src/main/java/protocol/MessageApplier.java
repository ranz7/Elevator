package protocol;

import gates.Gates;

/**
 * @see Gates
 * @see AppController
 * @see GuiController
 *  Listener of messages. If popMessage returns true, then message would be deleted.
 */
public interface MessageApplier {
    boolean applyMessage(ProtocolMessage message);
}
