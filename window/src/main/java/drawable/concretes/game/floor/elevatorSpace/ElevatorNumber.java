package drawable.concretes.game.floor.elevatorSpace;

import drawable.concretes.game.elevator.FloorGetter;
import lombok.Getter;
import lombok.Setter;
import model.Transport;
import model.Transportable;
import settings.RoomRemoteSettings;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.text.Text;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

public class ElevatorNumber extends DrawableCreature {
    FloorGetter getter;

    public ElevatorNumber(Vector2D position, FloorGetter getter, LocalDrawSetting settings) {
        super(position, new Vector2D(100, 15), new Text("-", settings.colorOfNumber()), settings);
    this.getter = getter;
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomCenter;
    }

    @Override
    public int getDrawPriority() {
        return 14;
    }

    @Override
    public void tick(double deltaTime) {
        String numberOfFloor = getter.getCurrentFloorNum() + "";
        ((Text) getTool()).setText(numberOfFloor);
    }

}
