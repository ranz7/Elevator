package controller;

import model.WindowModel;

public class Main {
    public static void main(String[] args) {
        var windowModel = new WindowModel();
        var controller = new WindowController(windowModel);

        try {
            controller.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
