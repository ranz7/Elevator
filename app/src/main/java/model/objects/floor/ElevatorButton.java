package model.objects.floor;

import lombok.Getter;
import lombok.Setter;
import model.Transport;
import model.Transportable;
import model.objects.Creature;
import settings.LocalCreaturesSettings;
import tools.Vector2D;

public class ElevatorButton extends Creature implements Transportable {
    @Getter
    @Setter
    Transport transport;

    public ElevatorButton(Double position, LocalCreaturesSettings settings) {
        super(new Vector2D(position, settings.elevatorSize().y / 2).add(settings.buttonRelativePosition()),
                settings.buttonSize());
    }

    public void click(boolean wantsGoUp) {

    }

}
