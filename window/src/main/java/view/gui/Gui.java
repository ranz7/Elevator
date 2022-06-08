package view.gui;

import controller.GuiController;
import lombok.Getter;
import controller.Tickable;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;
import view.gui.windowReacts.ButtonsReact;
import view.gui.windowReacts.MouseReact;
import view.gui.windowReacts.ResizeReact;

import java.awt.*;

public class Gui implements Tickable, ButtonsReact, MouseReact, ResizeReact {
    private final GuiController controller;
    @Getter
    private final SwingWindow swingWindow;
    //  @Getter
    //  private final ButtonsComponent buttonsComponent;

    public Gui(GuiController controller, LocalDrawSetting localDrawSetting) {
        this.controller = controller;
        swingWindow = new SwingWindow(this, localDrawSetting);
        resize();
        //    buttonsComponent = new ButtonsComponent(gameWindow);
        //   buttonsComponent.addButtonListener(this);
    }

    public void start() {
        //   buttonsComponent.start();
    }

    @Override
    public void tick(double deltaTime) {
        swingWindow.tick(deltaTime);
        var mouseLocation = swingWindow.getMousePosition();
        if (mouseLocation != null) {
            controller.getActivePlane().mousePositionUpdate(mouseLocation);
        }
    }

    @Override
    public void rightMouseClicked(Vector2D point) {
        controller.getActivePlane().rightMouseClicked(point);
    }


    public void draw(Graphics g) {
        controller.getMenu().draw(g);
    }

    @Override
    public void resize() {
        controller.getMenu().resize(new Vector2D(swingWindow.getSize()));
//        worldScaler.updateSizes(swingWindow.getSize(), new Vector2D(100, 100));
        //    buttonsComponent.resize(gameWindow.getSize());
    }

    @Override
    public void leftMouseClicked(Vector2D point) {
        controller.getActivePlane().leftMouseClicked(point);
        controller.getActivePlane().executeButtonIfCan();
    }
//
//    @Override
//    public void addElevatorButtonClicked() {
//        controller.changeElevatorsCount(true);
//    }
//
//    @Override
//    public void removeElevatorButtonClicked() {
//        controller.changeElevatorsCount(false);
//    }
//
//    @Override
//    public void decreaseGameSpeedButtonClicked() {
//        controller.decreaseGameSpeed();
//    }
//
//    @Override
//    public void increaseGameSpeedButtonClicked() {
//        controller.increaseSpeed();
//    }
//
//    @Override
//    public void clickedStartEndFloorButtonRequest(int start, int end) {
//        controller.addCustomer(start, end);
//    }
}

