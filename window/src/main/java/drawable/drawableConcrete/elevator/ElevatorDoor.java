package drawable.drawableConcrete.elevator;

import configs.tools.CombienedDrawDataBase;
import drawable.drawableAbstract.DrawableLocalMoving;
import drawable.drawableAbstract.drawableWithTexture.DrawCenter;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Timer;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

public class ElevatorDoor extends DrawableLocalMoving {
    private double gap;
    private final Timer doorsTimer = new Timer();
    private boolean isClosed = true;
    private final boolean isLeftDoor;

    public ElevatorDoor(Vector2D position, Vector2D size, boolean isLeftDoor, Trajectory trajecotry, CombienedDrawDataBase settings) {
        super(
                position.getAdded(
                        isLeftDoor ?
                                (new Vector2D(0, 0)) :
                                (new Vector2D(-size.x / 2., 0))),
                size, trajecotry, settings);
        this.isLeftDoor = isLeftDoor;
    }

    @Override
    public DrawCenter getDrawCenter() {
        if (isLeftDoor) {
            return DrawCenter.MIDDLE_BY_X;
        } else {
            return DrawCenter.RIGHT_BY_X;
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
        if (!isClosed) {
            percentage = 1 - percentage;
        }

        gap = percentage * getSize().x / 2.;
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 6;
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