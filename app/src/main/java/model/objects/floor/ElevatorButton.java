package model.objects.floor;

import controller.subControllers.ElevatorsController;
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
    ElevatorsController elevatorController;
    LocalCreaturesSettings settings;

    public ElevatorButton(Double position, ElevatorsController elevatorController, LocalCreaturesSettings settings) {
        super(new Vector2D(position, settings.elevatorSize().y / 4).add(settings.buttonRelativePosition()),
                settings.buttonSize());
        this.elevatorController = elevatorController;
        this.settings = settings;
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
    }

    public void click(boolean wantsGoUp) {
        elevatorController.buttonClick(
                ((FloorStructure) transport).getCurrentFloorNum(),
                this,
                wantsGoUp);
    }

}
