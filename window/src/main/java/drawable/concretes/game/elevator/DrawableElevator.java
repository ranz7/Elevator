package drawable.concretes.game.elevator;

import drawable.abstracts.Drawable;
import drawable.concretes.game.customer.DrawableCustomer;
import drawable.concretes.game.floor.DrawableFloorStructure;
import drawable.concretes.game.floor.decorations.FloorPainting;
import drawable.concretes.game.floor.elevatorSpace.ElevatorButton;
import lombok.Setter;
import model.DatabaseOf;
import model.Transport;
import model.Transportable;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableRemoteCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import model.packageLoader.DrawableCreatureData;
import protocol.special.CreatureType;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;
import lombok.Getter;

import java.util.Random;

@Getter
public class DrawableElevator extends DrawableRemoteCreature implements Transport<Drawable>, Transportable<Drawable>, FloorGetter {
    @Setter
    @Getter
    private Transport<Drawable> transport;
    @Getter
    private final DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this, ElevatorDoor.class, DrawableCustomer.class);

    private final ElevatorDoor leftDoor;
    private final ElevatorDoor rightDoor;


    public DrawableElevator(DrawableCreatureData creatureData, double OpenCloseTime, LocalDrawSetting settings) {
        super(creatureData, new Rectangle(settings.elevatorBackGroundColor()), settings);
        leftDoor = new ElevatorDoor(
                new Vector2D(-this.getSize().x / 2, 0), this.getSize(),
                true, settings, OpenCloseTime);
        rightDoor = new ElevatorDoor(
                new Vector2D(this.getSize().x / 2, 0), this.getSize(),
                false, settings, OpenCloseTime);

        add(leftDoor);
        add(rightDoor);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomCenter;
    }

    @Override
    public int getDrawPriority() {
        return 4;
    }

    public void changeDoorsState(boolean state) {
        int type = new Random().nextInt(5);
        leftDoor.changeDoorState(state, type);
        rightDoor.changeDoorState(state, type);
    }

    public int getCurrentFloorNum() {
        return ((DrawableFloorStructure) transport).getCurrentFloorNum();
    }

    @Override
    public void add(Drawable drawable) {
        if (drawable instanceof DrawableCreatureData) {
            if (((DrawableCreatureData) drawable).getCreatureType() == CreatureType.CUSTOMER) {
                drawable = new DrawableCustomer((DrawableCreatureData) drawable, getSettings());
            } else{
                throw new RuntimeException("uncached " + ((DrawableCreatureData) drawable).getCreatureType());
            }
        }
        localDataBase.addCreature(drawable);
    }
}
