package model.objects;

import configs.ConnectionSettings;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * Contains all data about creatures to send.
 * <p>
 * This data is sending SSPS times at second
 *
 * @see ConnectionSettings
 * </p>
 */
@RequiredArgsConstructor
public class CreaturesData implements Serializable {
    public final List<Creature> CUSTOMERS;
    public final List<Creature> ELEVATORS;
}
