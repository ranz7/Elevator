
package view.gui;


import controller.GuiController;
import drawable.drawableObjectsConcrete.FlyingText;
import lombok.Getter;
import model.GuiModel;
import tools.Vector2D;
import view.canvas.GameWindow;
import view.buttons.ButtonsComponent;

import java.awt.*;

public class Gui {
    private final GuiController controller;

    @Getter
    private final GuiModel guiModel;
    @Getter
    private final GameWindow gameWindow;
    @Getter
    private final ButtonsComponent buttonsComponent;

    public Gui(GuiModel guiModel, GuiController controller) {
        this.controller = controller;
        this.guiModel = guiModel;

        gameWindow = new GameWindow(guiModel);
        buttonsComponent = new ButtonsComponent(this, guiModel, gameWindow);

        buttonsComponent.addButtonsListener(this, new ButtonsListener(buttonsComponent));
        gameWindow.addMouseListener(new WindowMouseListener(this));
        gameWindow.addResizeListener(new WindowResizeListener(this));
    }

    public void start() {
        gameWindow.start();
        buttonsComponent.start();
        resize();
    }

    public void update() {
        gameWindow.update();
    }

    public void resize() {
        buttonsComponent.resize(gameWindow.getSize());
        gameWindow.resize(gameWindow.getSize());
    }

    public void rightMouseClicked(Vector2D point) {
        if (gameWindow.zoomedIn()) {
            guiModel.addMovingDrawable(
                    new FlyingText("ZoomIn", gameWindow.getGAME_SCALER().getFromRealToGameCoordinate(point, 4),
                            Vector2D.North, 6, 5, 1000, new Color(255, 100, 100)));
            gameWindow.zoomIn(point, 1 / 4.);
        } else {
            gameWindow.zoomOut();
        }
    }

    public void leftMouseClicked(Vector2D point) {
        controller.clickButton(point);
        guiModel.addMovingDrawable(
                new FlyingText("Click", gameWindow.getGAME_SCALER().getFromRealToGameCoordinate(point, 4),
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

