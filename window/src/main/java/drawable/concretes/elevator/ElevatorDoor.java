package drawable.concretes.elevator;

import databases.CombienedDrawDataBase;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import model.objects.Creature;
import tools.Timer;
import tools.Vector2D;

public class ElevatorDoor extends DrawableCreature {
    private final Timer doorsTimer = new Timer();
    private final boolean isLeftDoor;
    private boolean isClosed = true;
    private Vector2D openedDoorsSize;

    public ElevatorDoor(Vector2D position, Vector2D size, boolean isLeftDoor, CombienedDrawDataBase settings) {
        super(new Creature(position, size),
                new RectangleWithBorder(settings.doorsColor(), settings.doorsBorder(), 2),
                settings);
        openedDoorsSize = getSize();
        this.isLeftDoor = isLeftDoor;
    }

    @Override
    public DrawCenter getDrawCenter() {
        if (isLeftDoor) {
            return DrawCenter.bottomLeft;
        } else {
            return DrawCenter.bottomRight;
        }
    }

    @Override
    public void tick(double deltaTime) {
        doorsTimer.tick(deltaTime);
        double percentage = doorsTimer.getPercent();
        if (isClosed) {
            percentage = 1 - percentage;
        }
        setSize(openedDoorsSize.getMultiplied(new Vector2D(percentage / 2, 1)));
    }

    @Override
    public int GetDrawPrioritet() {
        return 14;
    }

    public void changeDoorState(boolean newState) {
        if (isClosed == newState) {
            return;
        }
        doorsTimer.restart(dataBase.elevatorOpenCloseTime() / 2);
        isClosed = !isClosed;
    }

    public boolean isClosed() {
        return isClosed && doorsTimer.isReady();
    }
}