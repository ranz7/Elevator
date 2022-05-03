
package view.Gui;


import controller.GuiController;
import drawable.drawableObjects.FlyingText;
import lombok.Getter;
import model.GuiModel;
import tools.Vector2D;
import view.canvas.GameWindow;
import view.buttons.ButtonsComponent;

import java.awt.*;

public class Gui {
    private GuiController controller;
    private GuiModel guiModel;

    @Getter
    private ButtonsComponent buttonsComponent;

    @Getter
    private GameWindow gameWindow;
    public Gui(GuiModel guiModel, GuiController controller){
        this.controller = controller;
        this.guiModel = guiModel;
        gameWindow = new GameWindow(guiModel);
        buttonsComponent = new ButtonsComponent( guiModel, gameWindow);

        buttonsComponent.addButtonsListener(this,new ButtonsListener(buttonsComponent));
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
                    new FlyingText("ZoomIn", gameWindow.getGameScaler().getFromRealToGameCoordinate(point, 4),
                            Vector2D.North, 6, 5, 1000, new Color(255, 100, 100)));
            gameWindow.zoomIn(point, 1 / 4.);
        } else {
            gameWindow.zoomOut();
        }
    }


    public void leftMouseClicked(Vector2D point) {
        controller.clickButton(point);
        guiModel.addMovingDrawable(
                new FlyingText("Click", gameWindow.getGameScaler().getFromRealToGameCoordinate(point, 4),
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

