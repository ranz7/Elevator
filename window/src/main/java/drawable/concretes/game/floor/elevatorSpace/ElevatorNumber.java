package drawable.concretes.game.floor.elevatorSpace;

import lombok.Getter;
import lombok.Setter;
import model.Transport;
import model.Transportable;
import settings.CombienedDrawSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.text.Text;
import tools.Vector2D;

public class ElevatorNumber extends DrawableCreature implements Transportable {
    @Getter
    @Setter
    Transport transport;

    protected ElevatorNumber(Vector2D position, CombienedDrawSettings settings) {
        super(position, new Vector2D(100, 15), new Text("-", settings.colorOfNumber()), settings);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomCenter;
    }

    @Override
    public int GetDrawPrioritet() {
        return 14;
    }

    @Override
    public void tick(double deltaTime) {
        String numberOfFloor = ((ElevatorBorder) transport).getCurrentFloorOfElevator() + "";
        ((Text) getTool()).setText(numberOfFloor);
    }

}
