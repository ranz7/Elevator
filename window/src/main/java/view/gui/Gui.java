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

        //    buttonsComponent = new ButtonsComponent(gameWindow);
        //   buttonsComponent.addButtonListener(this);
    }

    public void start() {
        //   buttonsComponent.start();
    }

    @Override
    public void tick(double deltaTime) {
        swingWindow.tick(deltaTime);
    }

    @Override
    public void rightMouseClicked(Vector2D point) {
        controller.getActivePlane().rightMouseClicked(point);
    }


    public void draw(Graphics g) {
        controller.getAllPlanes().forEach(plane -> plane.draw(g));
    }

    @Override
    public void resize() {
        controller.getActivePlane().resize(swingWindow.getSize());
//        worldScaler.updateSizes(swingWindow.getSize(), new Vector2D(100, 100));
        //    buttonsComponent.resize(gameWindow.getSize());
    }

    @Override
    public void leftMouseClicked(Vector2D point) {
        controller.getActivePlane().leftMouseClicked(point);
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

