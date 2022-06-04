package drawable.concretes.game.elevator;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import settings.localDraw.LocalDrawSetting;
import tools.Timer;
import tools.Vector2D;

public class ElevatorDoor extends DrawableCreature {
    private final Timer doorsTimer = new Timer();
    private final boolean isLeftDoor;
    private boolean isClosed = true;
    private Vector2D openedDoorsSize;
    private final double openCloseTime;
    private int drawPriority = 14;

    public ElevatorDoor(Vector2D position, Vector2D size, boolean isLeftDoor, LocalDrawSetting settings,
                        double openCloseTime) {
        super(position, size, new RectangleWithBorder(settings.doorsColor(), settings.doorsBorder(), 2),
                settings);
        openedDoorsSize = getSize();
        this.isLeftDoor = isLeftDoor;
        this.openCloseTime = openCloseTime;
    }

    public ElevatorDoor(Vector2D position, Vector2D size, boolean isLeftDoor, LocalDrawSetting settings,
                        double openCloseTime, int drawPriority) {
        this(position, size, isLeftDoor, settings, openCloseTime);
        this.drawPriority = drawPriority;
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
        setSize(openedDoorsSize.multiply(new Vector2D(percentage / 2, 1)));
    }

    @Override
    public int getDrawPriority() {
        return drawPriority;
    }

    public void changeDoorState(boolean newState) {
        if (isClosed == newState) {
            return;
        }
        doorsTimer.restart(openCloseTime / 2);
        isClosed = !isClosed;
    }

    public void updateSize(Vector2D size) {
        this.openedDoorsSize = size;
        if (!isLeftDoor)
            setPosition(new Vector2D(size.x, 0));
    }

    public boolean isClosed() {
        return isClosed && doorsTimer.isReady();
    }
}