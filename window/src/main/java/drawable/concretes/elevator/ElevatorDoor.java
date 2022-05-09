package drawable.concretes.elevator;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.withShape.moving.DrawableLocalMoving;
import drawable.abstracts.DrawCenter;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Timer;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

public class ElevatorDoor extends DrawableLocalMoving {
    private double gap;
    private final Timer doorsTimer = new Timer();
    private boolean isClosed = true;
    private final boolean isLeftDoor;

    private Vector2D openedDoorsSize;

    public ElevatorDoor(Vector2D position, Vector2D size, boolean isLeftDoor, Trajectory trajecotry, CombienedDrawDataBase settings) {
        super(new Vector2D(0,0),
                size, trajecotry, settings);
        openedDoorsSize = getSize();
        System.out.println(getPosition());
        this.isLeftDoor = isLeftDoor;
    }

    @Override
    public DrawCenter getDrawCenter() {
        if (isLeftDoor) {
            return DrawCenter.bottomLeft;
        } else {
            return DrawCenter.bottomCenter;
        }
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.draw(this, settings.doorsColor(), settings.doorsBorder(), 2);
/*
        gameDrawer.drawFilledRect(getPosition().getAdded(new Vector2D(openedGap, 0)),
                new Vector2D((getSize().x / 2 - openedGap), getSize().y), doorsBorder, 2);
*/
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
        doorsTimer.tick(deltaTime);
        double percentage = doorsTimer.getPercent();
        if (isClosed) {
            percentage = 1 - percentage;
        }

        gap = percentage / 2;
        setSize(openedDoorsSize.getMultiplied(new Vector2D(gap, 1)));
    }


    @Override
    public Integer GetDrawPrioritet() {
        return 100;
    }

    @Override
    public boolean isDead() {
        return false;
    }

    public void changeDoorState(boolean newState) {
        if (isClosed == newState) {
            return;
        }
        doorsTimer.restart(settings.elevatorOpenCloseTime() / 2);
        isClosed = !isClosed;
    }

    public boolean isClosed() {
        return isClosed && doorsTimer.isReady();
    }
}