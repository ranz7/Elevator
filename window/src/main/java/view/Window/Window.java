
package view.Window;


import controller.WindowController;
import drawable.drawableObjects.FlyingText;
import lombok.Getter;
import model.WindowModel;
import tools.Vector2D;
import view.Canvas.GameCanvas;
import view.Canvas.GuiMouseListener;
import view.buttons.ButtonsComponent;

import java.awt.*;

public class Window {
    private WindowController controller;
    private WindowModel windowModel;
    @Getter
    private GameCanvas gameCanvas;
    private ButtonsComponent buttonsComponent;
    @Getter
    private Dimension size = WindowSettings.WindowStartSize;

    public void startWindow(WindowModel windowModel, WindowController controller) {
        this.controller = controller;
        this.windowModel = windowModel;
        gameCanvas = new GameCanvas(this, windowModel);
        buttonsComponent = new ButtonsComponent(this, windowModel, gameCanvas);

        gameCanvas.addMouseListener(new GuiMouseListener(this));
        gameCanvas.addComponentListener(new GuiResizeListener(this));
        resize();
    }

    public void update() {
        gameCanvas.update();
    }

    public void resize() {
        size = gameCanvas.getSize();
        buttonsComponent.resize(size);
        gameCanvas.resize(size);
    }


    public void rightMouseClicked(Vector2D point) {
        if (gameCanvas.zoomedIn()) {
            windowModel.addMovingDrawable(
                    new FlyingText("ZoomIn", gameCanvas.getGameScaler().getFromRealToGameCoordinate(point, 4),
                            Vector2D.North, 6, 5, 1000, new Color(255, 100, 100)));
            gameCanvas.zoomIn(point, 1 / 4.);
        } else {
            gameCanvas.zoomOut();
        }
    }


    public void leftMouseClicked(Vector2D point) {
        controller.clickButton(point);
        windowModel.addMovingDrawable(
                new FlyingText("Click", gameCanvas.getGameScaler().getFromRealToGameCoordinate(point, 4),
                        Vector2D.North, 6, 15, 300, new Color(255, 217, 13)));
    }

    public void addElevatorButtonClicked() {
        controller.changeElevatorsCount(true);
    }

    public void removeElevatorButtonClicked() {
        controller.changeElevatorsCount(false);
    }

    public void decreaseGameSpeedButtonClicked() {
        controller.decreaseGameSpeed();
    }

    public void increaseGameSpeedButtonClicked() {
        controller.increaseSpeed();
    }

    public void clickedStartEndFloorButtonRequest(int start, int end) {
        controller.clickedAddCustomerButtonWithNumber(start, end);
    }
}

