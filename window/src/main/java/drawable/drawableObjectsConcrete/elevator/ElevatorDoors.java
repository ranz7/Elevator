package drawable.drawableObjectsConcrete.elevator;

import configs.tools.CombienedDrawDataBase;
import drawable.drawableAbstract.DrawableLocalMoving;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;
import tools.Timer;

import java.awt.*;

public class ElevatorDoors extends DrawableLocalMoving {
    private final long openCloseDoorsTime;
    private final DrawableElevator parentElevator;
    private final Timer doorsTimer = new Timer();

    private boolean isClosed = true;
    private final Color doorsColor;
    private final Color doorsBorder;

    public ElevatorDoors(DrawableElevator parentElevator, CombienedDrawDataBase settings) {
        super(parentElevator.getPosition(), parentElevator.getSize(),
                Trajectory.MomentarilyPositionChange(parentElevator.getPosition()), settings);
        this.parentElevator = parentElevator;
        openCloseDoorsTime = settings.elevatorOpenCloseTime();
        this.doorsColor = settings.doorsColor();
        this.doorsBorder = settings.doorsBorder();
    }

    public void changeDoorsState(boolean newState) {
        if (isClosed == newState) {
            return;
        }
        doorsTimer.restart(openCloseDoorsTime / 2);
        isClosed = !isClosed;
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 6;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        double percentage = doorsTimer.getPercent();
        if (!parentElevator.getIsVisible()) {
            return;
        }
        if (!isClosed) {
            percentage = 1 - percentage;
        }

        var openedGap = percentage * getSize().x / 2.;

        gameDrawer.setColor(doorsColor);
        gameDrawer.drawRect(getPosition().getAdded(new Vector2D(-getSize().x / 2., 0)),
                new Vector2D((getSize().x / 2 - openedGap), getSize().y), doorsBorder, 2);

        gameDrawer.setColor(doorsColor);
        gameDrawer.drawRect(getPosition().getAdded(new Vector2D(openedGap, 0)),
                new Vector2D((getSize().x / 2 - openedGap), getSize().y), doorsBorder, 2);

    }

    public void tick(long deltaTime) {
        super.tick(deltaTime);
        doorsTimer.tick(deltaTime);
    }

    @Override
    public boolean isDead() {
        return false;
    }

    public boolean isClosed() {
        return isClosed && doorsTimer.isReady();
    }

}
