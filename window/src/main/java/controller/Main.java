package controller;

import model.GuiModel;

public class Main {
    public static void main(String[] args) {
        var controller = new GuiController();
        var guiModel = new GuiModel();
        controller.setModel(guiModel);
        controller.start();
    }
}
