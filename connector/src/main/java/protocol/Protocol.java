package protocol;


/**
 * Decide the type of the arrived protocol message.
 *
 * @see ProtocolMessage
 */

public enum Protocol {
    //-- BOTH --//
    //-- FROM_SERVER  --//
    HELLO_MESSAGE,                  // No (just greeting)
    ELEVATOR_BUTTON_CLICK,          // Vector2D (position of button)
    CUSTOMER_GET_IN_OUT,            // Integer (customer id)
    ELEVATOR_OPEN,                  // Integer (elevator id)
    ELEVATOR_CLOSE,                 // Integer (elevator id)
    UPDATE_DATA,                    // ApplicationCreatures
    WORLDS_PREPARE_SETTINGS,           // AApplicationSettings

    //-- FROM_CLIENT --//
    CHANGE_GAME_SPEED,              // Double (new game speed)
    CREATE_CUSTOMER,                // LinkedList<Integer> size of 2 (start floor id and end floor id)
    CHANGE_ELEVATORS_COUNT,          // bool ( add or remove elevator if possible)
    SUBSCRIBE_FOR,          // SubscribeRequest  (subscribe or unsubscribe from world id)


}
