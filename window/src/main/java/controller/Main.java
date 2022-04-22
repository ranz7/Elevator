package controller;


import model.WindowModel;
import view.SwingWindow;

public class Main {
    public static void main(String[] args) {
        var windowModel = new WindowModel();
        var controller = new WindowController(windowModel);
        SwingWindow GUI = new SwingWindow();
        GUI.startWindow();

        GUI.repaint();
    }
}
