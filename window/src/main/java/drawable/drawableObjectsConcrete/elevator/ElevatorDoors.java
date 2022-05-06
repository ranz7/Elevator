package drawable.drawableObjectsConcrete.elevator;

import configs.CanvasSettings.MainSettings;
import drawable.drawableBase.Drawable;
import tools.Vector2D;
import view.drawTools.GameDrawer;
import tools.Timer;

import java.awt.*;

public class ElevatorDoors extends Drawable {
    private final long openCloseDoorsTime;
    private final DrawableElevator parentElevator;
    private final Timer doorsTimer = new Timer();

    private boolean isClosed = true;
    private final Color doorsColor;
    private final Color doorsBorder;

    public ElevatorDoors(DrawableElevator parentElevator, MainSettings settings) {
        super(parentElevator);
        size.x += 7;
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
        if (!parentElevator.isVisible()) {
            return;
        }
        if (!isClosed) {
            percentage = 1 - percentage;
        }
        var openedGap = percentage * size.x / 2.;

        gameDrawer.setColor(doorsColor);
        gameDrawer.drawFilledRect(position.getAdded(new Vector2D(-size.x / 2., 0)),
                new Vector2D((int) (size.x / 2 - openedGap), size.y), doorsBorder, 2);

        gameDrawer.setColor(doorsColor);
        gameDrawer.drawFilledRect(position.getAdded(new Vector2D(openedGap, 0)),
                new Vector2D((int) (size.x / 2 - openedGap), size.y), doorsBorder, 2);

    }

    public void tick(long delta_time) {
        position = parentElevator.getPosition();
        doorsTimer.tick(delta_time);
    }

    public boolean isClosed() {
        return isClosed && doorsTimer.isReady();
    }

}
