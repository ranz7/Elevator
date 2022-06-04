package drawable.concretes.game.elevator;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import settings.localDraw.LocalDrawSetting;
import tools.Timer;
import tools.Vector2D;

import java.util.Random;

public class ElevatorDoor extends DrawableCreature {
    private final Timer doorsTimer = new Timer();
    private final boolean isLeftDoor;
    private boolean isClosed = true;
    private final Vector2D openedDoorsSize;
    private final double openCloseTime;
    private int randomFunction = 0;

    public ElevatorDoor(Vector2D position, Vector2D size, boolean isLeftDoor, LocalDrawSetting settings,
                        double openCloseTime) {
        super(position, size,
                new RectangleWithBorder(settings.doorsColor(), settings.doorsBorder(), 2),
                settings);
        openedDoorsSize = getSize();
        this.isLeftDoor = isLeftDoor;
        this.openCloseTime = openCloseTime;
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

        setSize(openedDoorsSize.multiply(new Vector2D(doorOpenFunction(percentage) / 2, 1)));
    }

    private double doorOpenFunction(double x) {
        if (randomFunction == 0) {
            return Math.pow(x, 0.7);
        } else if (randomFunction == 1) {
            return x / (Math.pow(Math.E, x - 1));
        } else if (randomFunction == 2) {
            double bumb = -(Math.pow(x - 0.4, 2) * 10) + 0.5;
            bumb += Math.abs(bumb);
            bumb /= 6;
            return x / (Math.pow(Math.E, x - 1)) + bumb;
        } else if (randomFunction == 3) {
            double bumb = -(Math.pow(x - 0.8, 2) * 10) + 0.4;
            bumb += Math.abs(bumb);
            bumb /= 5;
            return x / (Math.pow(Math.E, x - 1)) - bumb;
        } else {
            double bumb = -(Math.pow(x - 0.8, 2) * 10) + 0.4;
            bumb += Math.abs(bumb);
            bumb /= 5;

            double bumb2 = -(Math.pow(x - 0.3, 2) * 9) + 0.7;
            bumb2 += Math.abs(bumb2);
            bumb2 /= 9;
            return x / (Math.pow(Math.E, x - 1)) - bumb + bumb2;
        }
    }

    @Override
    public int getDrawPrioritet() {
        return 14;
    }

    public void changeDoorState(boolean newState, int type) {
        if (isClosed == newState) {
            return;
        }
        doorsTimer.restart(openCloseTime / 2);
        isClosed = !isClosed;
        randomFunction = type ;
    }

    public boolean isClosed() {
        return isClosed && doorsTimer.isReady();
    }
}