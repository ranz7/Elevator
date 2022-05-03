package connector.protocol;


/**
 * Decide the type of the arrived protocol message.
 *
 * @see ProtocolMessage
 */

public enum Protocol {
    //-- BOTH --//
    CHANGE_GAME_SPEED,              // Double (new game speed)
    //-- FROM_SERVER  --//
    ELEVATOR_BUTTON_CLICK,          // Vector2D (position of button)
    CUSTOMER_GET_IN_OUT,            // Integer (customer id)
    ELEVATOR_OPEN,                  // Integer (elevator id)
    ELEVATOR_CLOSE,                 // Integer (elevator id)
    UPDATE_DATA,                    // ApplicationCreatures
    APPLICATION_SETTINGS,           // AApplicationSettings

    //-- FROM_CLIENT --//
    CREATE_CUSTOMER,                // LinkedList<Integer> size of 2 (start floor and end floor)
    CHANGE_ELEVATORS_COUNT,         // bool ( add or remove elevator if possible)


}
