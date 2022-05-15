package connector.protocol;

import connector.Gates;

/**
 * @see Gates
 * @see controller.AppController
 *  Listener of messages. If popMessage returns true, then message would be deleted.
 */

<<<<<<< Updated upstream:common/src/main/java/connector/protocol/ProtocolMessageListener.java
public interface ProtocolMessageListener {
    boolean popMessage(ProtocolMessage message);
=======
public interface ProtocolMessagesController {
    boolean applyMessage(ProtocolMessage message);
>>>>>>> Stashed changes:common/src/main/java/connector/protocol/ProtocolMessagesController.java
}
