
package view.gui;


import controller.GuiController;
import drawable.drawableObjectsConcrete.FlyingText;
import lombok.Getter;
import model.GuiModel;
import tools.tools.Vector2D;
import view.canvas.GameWindow;
import view.buttons.ButtonsComponent;

import java.awt.*;

public class Gui {
    private final GuiController controller;

    @Getter
    private GuiModel guiModel;
    @Getter
    private final GameWindow gameWindow;
    @Getter
    private final ButtonsComponent buttonsComponent;

    public Gui(GuiController controller) {
        this.controller = controller;
        gameWindow = new GameWindow();
        buttonsComponent = new ButtonsComponent(this,  gameWindow);

        buttonsComponent.addButtonsListener(this, new ButtonsListener(buttonsComponent));
        gameWindow.addMouseListener(new WindowMouseListener(this));
        gameWindow.addResizeListener(new WindowResizeListener(this));
    }
    boolean started = false;

    public void start() {
        if (started) {
            return;
        }
        started = true;
        gameWindow.start();
        buttonsComponent.start();
        resize();
    }

    public void update() {
        gameWindow.update();
    }

    public void resize() {
<<<<<<< Updated upstream
=======
        if (!guiModel.getCombienedDrawSettings().initialized()) {
            return;
        }
        gameWindow.resize(gameWindow.getSize());
>>>>>>> Stashed changes
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

    public void setModel(GuiModel model) {
        guiModel = model;
        gameWindow.setModel(model);
        buttonsComponent.setModel(model);
    }
}

