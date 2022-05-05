package view.gui;

import controller.GuiController;
import drawable.drawableObjectsConcrete.FlyingText;
import lombok.Getter;
import model.GuiModel;
import architecture.tickable.Tickable;
import tools.Vector2D;
import view.canvas.GameWindow;
import view.buttons.ButtonsComponent;
import view.gui.windowListeners.WindowMouseListener;
import view.gui.windowListeners.WindowResizeListener;
import view.gui.windowReacts.ButtonsReact;
import view.gui.windowReacts.MouseReact;
import view.gui.windowReacts.ResizeReact;

import java.awt.*;

public class Gui implements Tickable, ButtonsReact, MouseReact, ResizeReact {
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
        gameWindow.addMouseListener(new WindowMouseListener(this));
        gameWindow.addResizeListener(new WindowResizeListener(this));


        buttonsComponent = new ButtonsComponent(gameWindow);
        buttonsComponent.addButtonListener(this);
    }

    public void start() {
        gameWindow.start();
        buttonsComponent.start();
    }

    @Override
    public void tick(long deltaTime) {
        gameWindow.tick(deltaTime);

    }

    @Override
    public void resize() {
        if (guiModel.getMainInitializationSettings() == null) {
            return;
        }
        gameWindow.resize(gameWindow.getSize());
        buttonsComponent.resize(gameWindow.getSize());
    }

    @Override
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

    @Override
    public void leftMouseClicked(Vector2D point) {
        controller.clickButton(point);
        guiModel.addMovingDrawable(
                new FlyingText("Click", gameWindow.getGameScaler().getFromRealToGameCoordinate(point, 4),
                        Vector2D.North, 6, 15, 300, new Color(255, 217, 13)));
    }

    @Override
    public void addElevatorButtonClicked() {
        controller.changeElevatorsCount(true);
    }

    @Override
    public void removeElevatorButtonClicked() {
        controller.changeElevatorsCount(false);
    }

    @Override
    public void decreaseGameSpeedButtonClicked() {
        controller.decreaseGameSpeed();
    }

    @Override
    public void increaseGameSpeedButtonClicked() {
        controller.increaseSpeed();
    }

    @Override
    public void clickedStartEndFloorButtonRequest(int start, int end) {
        controller.clickedAddCustomerButtonWithNumber(start, end);
    }

    public void setModel(GuiModel model) {
        guiModel = model;
        gameWindow.setModel(model);
        buttonsComponent.setModel(model);
    }
}

